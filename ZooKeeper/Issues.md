問題列表
=======

https://wiki.apache.org/hadoop/ZooKeeper/FAQ


以 Docker 做為 ZooKeeper 執行環境時的相關問題
-------------------------------------------


- ## 伺服器斷線

docker container rm 與 stop -> start （or restart）是不同的情況

  - `docker container rm` 再進行 `docker run` 等同重新安裝 ~~，會對 client socker 造成 `SESSIONEXPIRED`~~
  - `docker container restart` 則會讓 `ClientCnxn` 自行完成重建 connection

	
下列為 `ClientCnxn` 噴的 log，如果無法與 ZooKeeper 建立 connection 會一直嘗試

```
Opening socket connection to server foo.bar.com/xxx.xxx.xxx.xxx:1234. Will not attempt to authenticate using SASL (unknown error) 
Socket error occurred: eshop-ms.eastus.cloudapp.azure.com/40.117.250.192:8080: Connection refused: no further information 
```

雖然似乎沒甚麼影響，過一段時間**偶爾**會噴

```
Client session timed out, have not heard from server in 5856ms for sessionid
```


如果是重新執行 `docker run` 建立新的 container 則會噴

```
Socket connection established to foo.bar.com/xxx.xxx.xxx.xxx:1234, initiating session 
Unable to read additional data from server sessionid 0x10039eedb8d0000, likely server has closed socket, closing socket connection and attempting reconnect 
Opening socket connection to server foo.bar.com/xxx.xxx.xxx.xxx:1234. Will not attempt to authenticate using SASL (unknown error) 
```

然而若在家裡測試時，重新建立 container 後會跳 `SESSIONEXPIRED` 然後結束程序（invoke `notifyAll()`）


- ## `Tester` 測試程式對於 ZK 斷線的反應與 WebApp（EAP）不同

以 `container stop` 的情況而言，EAP 會一直重複

```
INFO  [com.newKinPo.util.CacheCoordinator] (ServerService Thread Pool -- 59-EventThread) process() None | Disconnected
WARN  [org.apache.zookeeper.SaslClientCallbackHandler] (ServerService Thread Pool -- 59-SendThread(eshop-ms.eastus.cloudapp.azure.com:8080)) Could not login: the Client is being asked for a password, but the ZooKeeper Client code does not currently support obtaining a password from the user. Make sure that the Client is configured to use a ticket cache (using the JAAS configuration setting 'useTicketCache=true)' and restart the Client. If you still get this message after that, the TGT in the ticket cache has expired and must be manually refreshed. To do so, first determine if you are using a password or a keytab. If the former, run kinit in a Unix shell in the environment of the user who is running this Zookeeper Client using the command 'kinit <princ>' (where <princ> is the name of the Client's Kerberos principal). If the latter, do 'kinit -k -t <keytab> <princ>' (where <princ> is the name of the Kerberos principal, and <keytab> is the location of the keytab file). After manually refreshing your cache, restart this Client. If you continue to see this message after manually refreshing your cache, ensure that your KDC host's clock is in sync with this host's clock.
WARN  [org.apache.zookeeper.ClientCnxn] (ServerService Thread Pool -- 59-SendThread(eshop-ms.eastus.cloudapp.azure.com:8080)) SASL configuration failed: javax.security.auth.login.FailedLoginException: PBOX000070: Password invalid/Password required Will continue connection to Zookeeper server without SASL authentication, if Zookeeper server allows it.
INFO  [org.apache.zookeeper.ClientCnxn] (ServerService Thread Pool -- 59-SendThread(eshop-ms.eastus.cloudapp.azure.com:8080)) Opening socket connection to server xxx.xxx.xxx/x.x.x.x
INFO  [com.newKinPo.util.CacheCoordinator] (ServerService Thread Pool -- 59-EventThread) process() None | AuthFailed
INFO  [org.apache.zookeeper.ClientCnxn] (ServerService Thread Pool -- 59-SendThread(eshop-ms.eastus.cloudapp.azure.com:8080)) Socket error occurred: xxx.xxx.xxx/x.x.x.x: Connection refused: no further information
```


- ## 已確認的問題

使用 Windows run 的 zookeeper server 如果在停止之後，刪除其 `dataDir` 下的檔案就會發生本文提及的主要問題，
Docker 由於 remove container 再執行 run 等於也是重新 create 整個環境，意味著 `dataDir` 也會不存在，
所以大致上可以視為是一樣的問題。
<br/><br/>


Docker run with `-v` option
---------------------------

目前不確定為什麼會有權限問題：


```console
$ sudo docker run --name zookeeper -v ~/volumes/zookeeper-2181/conf:/conf -p 2181:2181 zookeeper:3.4.13
/docker-entrypoint.sh: line 15: /conf/zoo.cfg: Permission denied
```


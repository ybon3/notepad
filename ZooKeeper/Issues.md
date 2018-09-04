問題列表
========

https://wiki.apache.org/hadoop/ZooKeeper/FAQ


## 前提假設：以 Docker 做為 ZooKeeper 執行環境


- ## 伺服器斷線

docker container rm 與 stop -> start （or restart）是不同的情況

	- `docker container rm` 再進行 `docker run` 等同重新安裝，會對 client socker 造成 `SESSIONEXPIRED`
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


KeeperState
-----------


參閱：https://zookeeper.apache.org/doc/r3.4.13/api/index.html

- ### SyncConnected: The client is in the connected state 
	最正常的連接狀態

- ### Expired: The serving cluster has expired this session
	ClientCnxn 不會嘗試重新連接，會關閉 socket，**ZooKeeper 的 instance 也不可再使用**
	
	要製造這個現象可以把網路斷開一段時間（看 server timeout 的設定）再恢復連線 
	
- ### Disconnected: The client is in the disconnected state
	ClientCnxn 會一直嘗試重新連接，而且當這個狀態發生時，
	`ZooKeeper.getState().isConnected()` 還不見得會變成 `false`

- ### AuthFailed / ConnectedReadOnly / SaslAuthenticated
	尚未使用到
	
先說結論，主要有兩種選擇：

1. 設定 log 的路徑，並將檔案路徑經由 VOLUME 設定到 OS 層提供存取
2. 啟用 syslog 並將 log 吐給 syslog

目前先採用 1. 的做法


Docker container 的資料夾 Mount 設定
-----------------------------------

`Dockerfile` 中 `VOLUME` 的設定值可以指定 container 中的路徑有哪些可以對應到 OS 系統路徑


### `run` 指定使用 `-v` 參數設定 mount 目錄

```console
docker run -p 8090:8090 -v /mnt/orderms:/tmp dante0312/orderms
```


上面指令會將運行的 container 中的 `/tmp` mount 到 OS 層的實體路徑 `/mnt/orderms`，  
所有對 `/mnt/orderms` 資料夾中的存取行為等同對 container 中的 `/tmp` 進行存取。


### Dockerfile 的 VOLUME 設定

VOLUME 設定值**無法**指定 OS 層的實體路徑，對應的路徑由 Docker 產生，  
可使用下列命令調閱：

```console
docker inspect --format='{{.Mounts}}' $(docker ps -q) CONTAINER_ID
```

VOLUME 設定範例：

```
VOLUME /tmp
```


設定多個 

```
VOLUME ["/data1","/data2"]
```


### Windows 7 搭配 ToolBox 的環境

~~懶得囉嗦了~~參考下列命令吧 ...

```
docker run --name CONTAINER_NAME -v /c/Users/TB890057/tmp4docker:/tmp -p 8090:8090 dante0312/orderms
```



參考文件
-------

- [Docker: Sending Spring Boot logging to syslog](https://fabianlee.org/2017/03/21/docker-sending-spring-boot-logging-to-syslog/)

- [Docker Doc: Configure logging drivers](https://docs.docker.com/config/containers/logging/configure/)

- [docker学习笔记18：Dockerfile 指令 VOLUME 介绍](https://www.cnblogs.com/51kata/p/5266626.html)

- [Docker volume 簡單用法](https://julianchu.net/2016/04/19-docker.html)

- [入理解Docker Volume（一）](http://dockone.io/article/128)

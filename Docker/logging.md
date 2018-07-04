Docker container 的資料夾 Mount 設定
-----------------------------------

`Dockerfile` 中 `VOLUME` 的設定值可以指定 container 中的路徑有哪些可以對應到 OS 系統路徑


### `run` 指定使用 `-v` 參數設定 mount 目錄

```console
docker run -p 8090:8090 -v /mnt/orderms:/tmp dante0312/orderms
```


上面指令會將運行的 container 中的 `/tmp` mount 到 OS 層的實體路徑 `/mnt/orderms`，  
所有對 `/mnt/orderms` 資料夾中的存取行為等同對 container 中的 `/tmp` 進行存取。


參考文件
-------

- [Docker: Sending Spring Boot logging to syslog](https://fabianlee.org/2017/03/21/docker-sending-spring-boot-logging-to-syslog/)

- [Docker Doc: Configure logging drivers](https://docs.docker.com/config/containers/logging/configure/)

- [docker学习笔记18：Dockerfile 指令 VOLUME 介绍](https://www.cnblogs.com/51kata/p/5266626.html)

### 變更 Docker Machine 的 remote registry

參照：https://stackoverflow.com/questions/26026931/setting-up-a-remote-private-docker-registry

大致上有兩種方式：

1. 於啟動 service 命令追加參數：

```console
docker -d --insecure-registry 10.11.12.0:5000
```

2. 於設定檔 `/etc/default/docker` 追加：

```
DOCKER_OPTS="$DOCKER_OPTS --insecure-registry=192.168.2.170:5000"
```


### 從指定的 Registry pull image

```console
docker pull --allow-insecure internal.company.domain:5000/image_name
```


### Remove all images and containers

```console
docker rm $(docker ps -a -q)
```

```console
docker rmi $(docker images -q)
```


> `$(...)` 可以執行另一個指令，並將結果作為參數給主要指令使用


### 查閱 container 運作的詳細設定值

```console
docker inspect $(docker ps -q)
```


### Login to a private registry

```console
docker login localhost:8080
```

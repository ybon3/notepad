Reference: https://dotblogs.com.tw/swater111/2017/01/03/171042


### 查看 docker 已建立的 network

```console
docker network ls
```


### 查看指定 network 的內容

```console
docker network inspect bridge
```


### 查看一個 container 它拿到那一個IP地址

```console
sudo docker inspect --format='{{range .NetworkSettings.Networks}}{{.IPAddress}}{{end}}' container_name
```


### 建立自己的 network

```console
docker network create -d bridge MyNetwork
```

- - - -

另一份很好的圖說文件： https://kairen.github.io/2016/01/05/container/docker-network/

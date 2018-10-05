```console
docker run --name zookeeper --restart always -d -p 2888:2888 -p 3888:3888 -p 2181:2181 zookeeper:3.4.13
sudo docker run --name zookeeper --restart always -d -p 2888:2888 -p 3888:3888 -p 2181:2181 zookeeper:3.4.13
sudo docker run --name zookeeper -d -p 2888:2888 -p 3888:3888 -p 2181:2181 zookeeper:3.4.13
sudo docker run --name zookeeper -d -p 8080:2181 zookeeper:3.4.13
sudo docker run --name zookeeper -d -v ~/volumes/zookeeper/data:/data -p 8080:2181 zookeeper:3.4.13
```

## 登入 container 中的 zookeeper 執行介面

```console
sudo docker run -it --rm --link zookeeper:zookeeper zookeeper zkCli.sh -server zookeeper
```


## 開啟另一個 ZK 並串接另一個

```console
sudo docker run --name zookeeper -d -e "ZOO_SERVER_ID=1" -p 8080:2181 -p 2888:2888 -p 3888:3888 zookeeper:3.4.13
sudo docker run --name zookeeper2 -d -e "ZOO_SERVER_ID=2" -e "ZOO_SERVERS=172.17.0.2:2888:3888" -p 2181:2181 zookeeper:3.4.13
```


## 觀測 zookeeper 的狀態

```console
echo stat | nc 172.17.0.1 8080
echo mntr | nc 172.17.0.1 8080
echo isro | nc 172.17.0.1 8080
```

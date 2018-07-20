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


### Remove <none> TAG images 

```console
docker rm $(docker ps -a -q)
```

```console
docker rmi $(docker images | grep none | awk '{print $3;}')

sudo docker rmi $(sudo docker images | grep none | awk '{print $3;}')
```


### 查閱 container 運作的詳細設定值

```console
docker inspect $(docker ps -q)
```


取得 "Mounts" 的設定值

```console
docker inspect --format='{{.Mounts}}' $(docker ps -q)
```


# 使用 private registry 來管理 image

以下假設 private registry 的 host 為 `foo.bar.io`


### Login to a private registry

```console
docker login foo.bar.io
```


### 使用 `mvn dockerfile:build` 時 `pom.xml` 需設定為

```xml
...
<plugin>
	<groupId>com.spotify</groupId>
	<artifactId>dockerfile-maven-plugin</artifactId>
	<version>1.3.6</version>
	<configuration>
		<repository>foo.bar.io/${project.artifactId}</repository>
		<buildArgs>
			<JAR_FILE>target/${project.build.finalName}.jar</JAR_FILE>
		</buildArgs>
	</configuration>
</plugin>
```


### push image to private registry

```console
docker push foo.bar.io/wtf-ms
```


### pull image from private registry

於 deploy 環境中先用前面的登入指定完成登入，然後進行 `pull`

```console
sudo docker pull foo.bar.io/wtf-ms
```

最後以該 image 跑 container

```console
sudo docker run --name wtf-ms -d -v ~/volumes/wtf/tmp:/tmp -p 1234:1234 foo.bar.io/wtf-ms
```

*如果有需要可以加上 tag*

# Get started with Docker

Reference: https://docs.docker.com/get-started/

已完成安裝 `Docker Toolbox` (Windows 7)


## Part 1: Orientation and setup

主要介紹 Docker 的概念以及基本指令，本章指令總結：

```console
## List Docker CLI commands
docker
docker container --help

## Display Docker version and info
docker --version
docker version
docker info

## Execute Docker image
docker run hello-world

## List Docker images
docker image ls

## List Docker containers (running, all, all in quiet mode)
docker container ls
docker container ls --all
docker container ls -aq
```


## Part 2: Containers

#### 簡單教學如何建立一個 image：

- 撰寫一個 `Dockfile` 範例及其他需要的檔案
- build image into Docker

```
docker build -t friendlyhello .
```

- 執行

```
docker run -p 4000:80 friendlyhello

# or run in the background
docker run -d -p 4000:80 friendlyhello

# check machine ip
docker-machine ip
```

- 監控、關閉

```
docker container ls

# shutdown
docker container stop <Container NAME or ID>
```

#### 何謂 `registry`、`repository`

- `registry` 等於 `repository` 的 collection, `repository` 則是一堆 `image` 的 collection
- `registry` 像是 GitHub、GitLab 等 server 的存在
- `docker` CLI 預設使用 **Docker’s public registry**
- 我們可以使用其他的 `registry`，甚至是自己建立的（[Docker Trusted Registry](https://docs.docker.com/datacenter/dtr/2.2/guides/)）

#### 將 image 推上~~處刑台~~ Remote

- 註冊 docker id 並將本機 docker 登入

```
docker login
```

- 將 image tag 起來（非必要）

```
docker tag image username/repository:tag

# ex.
docker tag friendlyhello dante0312/get-started:part2
```

- push 

```
docker push username/repository:tag
```

一旦 push 上去之後，在任何 docker 環境只要執行（命令以範例為例）

```
docker run -p 4000:80 dante0312/get-started:part2
```

就應該要可以正常運作（當然前提是抓得到 Remote）

基本指令參考：

```console
docker build -t friendlyhello .  # Create image using this directory's Dockerfile
docker run -p 4000:80 friendlyhello  # Run "friendlyname" mapping port 4000 to 80
docker run -d -p 4000:80 friendlyhello         # Same thing, but in detached mode
docker container ls                                # List all running containers
docker container ls -a             # List all containers, even those not running
docker container stop <hash>           # Gracefully stop the specified container
docker container kill <hash>         # Force shutdown of the specified container
docker container rm <hash>        # Remove specified container from this machine
docker container rm $(docker container ls -a -q)         # Remove all containers
docker image ls -a                             # List all images on this machine
docker image rm <image id>            # Remove specified image from this machine
docker image rm $(docker image ls -a -q)   # Remove all images from this machine
docker login             # Log in this CLI session using your Docker credentials
docker tag <image> username/repository:tag  # Tag <image> for upload to registry
docker push username/repository:tag            # Upload tagged image to registry
docker run username/repository:tag                   # Run image from a registry
```

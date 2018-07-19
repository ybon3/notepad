查詢系統啟動的服務

```console
systemctl status
```


查詢 `docker` 服務

```console
$ systemctl status docker
● docker.service - Docker Application Container Engine
   Loaded: loaded (/usr/lib/systemd/system/docker.service; disabled; vendor preset: disabled)
   Active: active (running) since Thu 2018-07-19 02:36:14 UTC; 4min 10s ago
     Docs: http://docs.docker.com
 Main PID: 51954 (dockerd-current)
   CGroup: /system.slice/docker.service
           ├─51954 /usr/bin/dockerd-current --add-runtime docker-runc=/usr/libexec/docker/docker-runc-current --default-run...
           └─51959 /usr/bin/docker-containerd-current -l unix:///var/run/docker/libcontainerd/docker-containerd.sock --metr...
```

啟動 `docker`

```console
sudo systemctl start docker
```


設定 `docker` 於系統啟動時自動執行

```console 
sudo systemctl enable docker
```

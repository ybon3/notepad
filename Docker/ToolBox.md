docker-machine
--------------

refere: https://docs.docker.com/machine/reference/mount/

```script
$ mkdir foo
$ docker-machine ssh dev mkdir foo
$ docker-machine mount dev:/home/docker/foo foo
$ touch foo/bar
$ docker-machine ssh dev ls foo
bar
```

### 確認虛擬機器的 ip

```console
docker-machine ip
```


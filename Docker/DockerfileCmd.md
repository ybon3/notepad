Referemce: https://docs.docker.com/engine/reference/builder/#environment-replacement

Dockerfile 指令集
================


- FROM
- RUN
- CMD
- LABEL
- EXPOSE
- ENV
- ADD
- COPY
- ENTRYPOINT
- VOLUME
- USER
- WORKDIR
- ARG
- ONBUILD
- STOPSIGNAL
- HEALTHCHECK
- SHELL


.dockerignore file
==================

類似 `.gitignore` 大致上就是讓 `Dockerfile` 運行時忽略指定的檔案或目錄

Example:
```dockerfile
# comment
*/temp*
*/*/temp*
temp?
```

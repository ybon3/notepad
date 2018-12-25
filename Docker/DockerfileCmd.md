Referemce: https://docs.docker.com/engine/reference/builder/#environment-replacement

Dockerfile 指令集
================


### FROM
### RUN

### CMD
- **There can only be one CMD instruction in a Dockerfile.** If you list more than one CMD then only the last CMD will take effect.
- The main purpose of a CMD is to provide defaults for an executing container.
  
### LABEL
### EXPOSE
### ENV
### ADD
### COPY
### ENTRYPOINT
### VOLUME
### USER
### WORKDIR
### ARG
### ONBUILD
### STOPSIGNAL
### HEALTHCHECK
### SHELL


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

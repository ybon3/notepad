如何讓 Spring Boot 使用指定的 .properties 檔
==========================================

Spring 預設固定會啟用的是 application.properties，不論是否有特別指定。

### 命名規則：**application-{name}.properties**
<br/>
<br/>
以下以 `application-prod.properties` 為例：

- 透過執行 jar 的方式指定：

  ```console
  java -jar <your-jar>.jar --spring.profiles.active=prod  
  ```

- 在 docker-compose 中的指定方式：

  ```yml
  services:
    payment:
      image: nkgcontainerregistryeastasia.azurecr.io/payment-ms:latest
      container_name: payment-ms
      command: --spring.profiles.active=prod
  ```
  
- 在 Eclipse 中的指定方式：

  在 Project 上右鍵 -> `Run As` -> `Run Configurations...`
  
  右邊區塊切換道 `(x)= Arguments` tab，然後在 `Program arguments` 輸入區域加上

  ```console
  --spring.profiles.active=prod 
  ```

- 指定外部 .properties 檔案

  ```console
  java -jar -Dspring.config.location=/path/to/my/file.properties app.jar
  ```


Reference https://spring.io/guides/gs/spring-boot-docker/


如何將 Spring-Boot project build 到 Docker or Registry
-----------------------------------------------------

主要分下列步驟：

1. 新增 `dockerfile` 到 `pom.xml` 所在路徑
1. 在 `pom.xml` 中添加需要的 plugin
1. 專案建構
1. Build image or Push to Registry


### 新增 `dockerfile` 到 `pom.xml` 所在路徑

```
FROM openjdk:8-jdk-alpine
VOLUME /tmp
ARG JAR_FILE
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]
```


### 在 `pom.xml` 中添加需要的 plugin

```xml
<properties>
   <docker.image.prefix>springio</docker.image.prefix>
</properties>
<build>
    <plugins>
        <plugin>
            <groupId>com.spotify</groupId>
            <artifactId>dockerfile-maven-plugin</artifactId>
            <version>1.3.6</version>
            <configuration>
                <repository>${docker.image.prefix}/${project.artifactId}</repository>
                <buildArgs>
                    <JAR_FILE>target/${project.build.finalName}.jar</JAR_FILE>
                </buildArgs>
            </configuration>
        </plugin>
    </plugins>
</build>
```


> 注意事項：

- `JAR_FILE` 在 `pom.xml` 中透過 `<JAR_FILE>` 定義
- `<docker.image.prefix>` 對應 Registry 的帳號（預設的 Registry 是 [Docker Hub](https://hub.docker.com/))
- `<repository>` 指定 image name 為 `${project.artifactId}`，但 image name **必須全小寫**，
  所以如果專案名稱有大寫就會發生錯誤，你可以：
  
  - 另外指定 image name 
  - 避免專案名稱使用大寫
  
  
### 專案建構

執行 `mvn install`，並確認 jar 檔正確產生


### Build image or Push to Registry

將 JAR 包成 build 到 docker：

```
mvn dockerfile:build
```


將 JAR 包成 psuh 到 Registry：

```
mvn dockerfile:push
```

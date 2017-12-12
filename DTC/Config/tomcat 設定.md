## 如何指定 `dtc/config` 讀取 properties file

在 `conf/catalina.properties` 中設定：

```
shared.loader=${catalina.base}/dtc,${catalina.home}/dtc,${catalina.base}/dtc/*.jar,${catalina.home}/dtc/*.jar,${catalina.base}/dtc/config,${catalina.home}/dtc/config,${catalina.base}/dtc/config/*.jar,${catalina.home}/dtc/config/*.jar
```

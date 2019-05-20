事件時間： 2019/05/17

Reference: https://stackoverflow.com/questions/16223968/resteasy-jax-rs-in-jboss-7-1-doesnt-work

我本機的 EAP 6.4 會無法正確地將 `XYZStore` 載入，因為少了 `web.xml`

所以必須建立 `WebContent/web.xml`，路徑：

```
-pom.xml
-src
-WebContent
 |
  --web.xml
```

內容：

```xml
<?xml version="1.0" encoding="UTF-8"?>
<web-app xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns="http://java.sun.com/xml/ns/javaee" xsi:schemaLocation="http://java.sun.com/xml/ns/javaee http://java.sun.com/xml/ns/javaee/web-app_3_0.xsd" id="WebApp_ID" version="3.0">
  <display-name>XYZEShop</display-name>
  <servlet>
    <servlet-name>Resteasy</servlet-name>
    <servlet-class>org.jboss.resteasy.plugins.server.servlet.HttpServletDispatcher</servlet-class>
  </servlet>
  <servlet-mapping>
    <servlet-name>Resteasy</servlet-name>
    <url-pattern>/*</url-pattern>
  </servlet-mapping>
  <context-param>
    <param-name>javax.ws.rs.Application</param-name>
    <param-value>com.newKinPo.webservices.XYZStore</param-value>
  </context-param>
  <listener>
    <listener-class>org.jboss.resteasy.plugins.server.servlet.ResteasyBootstrap</listener-class>
  </listener>
</web-app>
```

Stanley 說應該是會自動產生，不確定為什麼我沒產生

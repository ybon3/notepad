傳輸壓縮功能設定
================

Tomcat 6.x
----------

一共有兩個檔案要設定

設定檔參考路徑 `${CATALINA_HOME}\conf\server.xml`

參考設定方式：
在 <Connector> 中加上 compression 等設定
```xml
<Connector connectionTimeout="20000"
	port="8080"
	protocol="HTTP/1.1"
	redirectPort="8443"
	compression="on"
	compressableMimeType="text/javascript,text/xml,text/html,text/css,text/plain,image/jpg,image/jpeg,image/png,image/gif"
	/> 
```

設定檔參考路徑 `${CATALINA_HOME}\conf\web.xml`

參考設定方式：
在 <servlet-name>default</servlet-name> 的設定內加上 sendfileSize 的設定
```xml
	<servlet>
        <servlet-name>default</servlet-name>
        <servlet-class>org.apache.catalina.servlets.DefaultServlet</servlet-class>
        <init-param>
            <param-name>debug</param-name>
            <param-value>0</param-value>
        </init-param>
        <init-param>
            <param-name>listings</param-name>
            <param-value>false</param-value>
        </init-param>
        <load-on-startup>1</load-on-startup>
        <init-param>
            <param-name>sendfileSize</param-name>
            <param-value>-1</param-value>
        </init-param>
    </servlet>
```

Tomcat 7.x & 8.x
----------------

設定檔參考路徑 `${CATALINA_HOME}\conf\server.xml`

參考設定方式：
```xml
<Connector connectionTimeout="20000"
	port="8080"
	protocol="HTTP/1.1"
	redirectPort="8443"
	useSendfile="false"
	compression="on"
	compressableMimeType="application/javascript,text/javascript,image/png,image/gif,image/jpg,image/bmp,text/html"
	compressionMinSize="2048"
	/>
```

- compressableMineType 的值可以依照要壓縮的來源檔案的content type 來進行調整，若不設定就代表所有傳輸的類型都會進行壓縮
- compressionMinSize 可以設定最小壓縮的檔案限制，系統會將大於此設定值的檔案進行壓縮，預設值是 2048，單位 Byte


注意事項
--------

在不同的 Tomcat 版本對於 javascript 的類型有不同的定義：
- Tomcat 6.x 是 text/javascript
- Tomcat 8.x 是 application/javascript

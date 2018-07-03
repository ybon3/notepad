Logging
=======

[offical reference](https://docs.spring.io/spring-boot/docs/current/reference/html/howto-logging.html)

`application.properties` 的相關設定值

```properties
# 設定 root level（所有未指定 logger level 的 package 都會參考這個設定，default 為 `INTO`）
logging.level.root=INFO

# 對 com.newkinpo.* 的 package 進行 level 設定
logging.level.com.newkinpo=DEBUG

# 設定 console 的 logger pattern
logging.pattern.console=[%d{HH:mm:ss}] %-5p %c - %m %n

# 設定 log file 
logging.file=${java.io.tmpdir}/application.log

```


`application.properties` 與 `logback.xml` 兩邊設定值的優先權不確定，已經確認的：

- `application.properties` 中的 `logging.level.root=INFO` 優先權**高於** `<root level="warn" />` 


<br/><br/>

### 下列設定參考 `spring-boot-2.0.3.RELEASE.jar` 中的設定檔

`base.xml`

```xml
<included>
	<include resource="org/springframework/boot/logging/logback/defaults.xml" />
	<property name="LOG_FILE" value="${LOG_FILE:-${LOG_PATH:-${LOG_TEMP:-${java.io.tmpdir:-/tmp}}}/spring.log}"/>
	<include resource="org/springframework/boot/logging/logback/console-appender.xml" />
	<include resource="org/springframework/boot/logging/logback/file-appender.xml" />
	<root level="INFO">
		<appender-ref ref="CONSOLE" />
		<appender-ref ref="FILE" />
	</root>
</included>
```
<br/>

`defaults.xml`

```xml
<included>
	<conversionRule conversionWord="clr" converterClass="org.springframework.boot.logging.logback.ColorConverter" />
	<conversionRule conversionWord="wex" converterClass="org.springframework.boot.logging.logback.WhitespaceThrowableProxyConverter" />
	<conversionRule conversionWord="wEx" converterClass="org.springframework.boot.logging.logback.ExtendedWhitespaceThrowableProxyConverter" />
	<property name="CONSOLE_LOG_PATTERN" value="${CONSOLE_LOG_PATTERN:-%clr(%d{${LOG_DATEFORMAT_PATTERN:-yyyy-MM-dd HH:mm:ss.SSS}}){faint} %clr(${LOG_LEVEL_PATTERN:-%5p}) %clr(${PID:- }){magenta} %clr(---){faint} %clr([%15.15t]){faint} %clr(%-40.40logger{39}){cyan} %clr(:){faint} %m%n${LOG_EXCEPTION_CONVERSION_WORD:-%wEx}}"/>
	<property name="FILE_LOG_PATTERN" value="${FILE_LOG_PATTERN:-%d{${LOG_DATEFORMAT_PATTERN:-yyyy-MM-dd HH:mm:ss.SSS}} ${LOG_LEVEL_PATTERN:-%5p} ${PID:- } --- [%t] %-40.40logger{39} : %m%n${LOG_EXCEPTION_CONVERSION_WORD:-%wEx}}"/>

	<logger name="org.apache.catalina.startup.DigesterFactory" level="ERROR"/>
	<logger name="org.apache.catalina.util.LifecycleBase" level="ERROR"/>
	<logger name="org.apache.coyote.http11.Http11NioProtocol" level="WARN"/>
	<logger name="org.apache.sshd.common.util.SecurityUtils" level="WARN"/>
	<logger name="org.apache.tomcat.util.net.NioSelectorPool" level="WARN"/>
	<logger name="org.eclipse.jetty.util.component.AbstractLifeCycle" level="ERROR"/>
	<logger name="org.hibernate.validator.internal.util.Version" level="WARN"/>
</included>
```
<br/>

`console-appender.xml`

```xml
<included>
	<appender name="CONSOLE" class="ch.qos.logback.core.ConsoleAppender">
		<encoder>
			<pattern>${CONSOLE_LOG_PATTERN}</pattern>
		</encoder>
	</appender>
</included>
```
<br/>

`file-appender.xml`

```xml
<included>
	<appender name="FILE"
		class="ch.qos.logback.core.rolling.RollingFileAppender">
		<encoder>
			<pattern>${FILE_LOG_PATTERN}</pattern>
		</encoder>
		<file>${LOG_FILE}</file>
		<rollingPolicy class="ch.qos.logback.core.rolling.SizeAndTimeBasedRollingPolicy">
			<fileNamePattern>${LOG_FILE}.%d{yyyy-MM-dd}.%i.gz</fileNamePattern>
			<maxFileSize>${LOG_FILE_MAX_SIZE:-10MB}</maxFileSize>
			<maxHistory>${LOG_FILE_MAX_HISTORY:-0}</maxHistory>
		</rollingPolicy>
	</appender>
</included>
```


LEVEL
-----

Standard Level | intLevel 
---------------|----------
OFF	  | 0
FATAL	| 100
ERROR	| 200
WARN	| 300
INFO	| 400
DEBUG	| 500
TRACE	| 600
ALL	  | `Integer.MAX_VALUE`


其他參考文件
-----------

- [Spring: Spring Boot with SLF4J/Logback sending to syslog](http://fabianlee.org/2017/03/20/spring-spring-boot-with-slf4j-logback-sending-to-syslog/)

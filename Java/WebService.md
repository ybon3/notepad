參考自：http://bioankeyang.blogspot.tw/2013/08/web-service.html

```java
package com.ws;

import javax.jws.WebService;
import javax.xml.ws.Endpoint;

@WebService
public class WsDemo {

	public String concat(String a, String b) {
		return a + b;
	}

	public static void main(String[] args){
		String url = "http://localhost:8080/demo";
		Endpoint.publish(url, new WsDemo());
		System.out.println("WsDemo Service published, url: " + url);
	}
}

```

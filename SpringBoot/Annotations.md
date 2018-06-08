## @EnableAutoConfiguration


## @Configuration



## @ComponentScan



## @SpringBootApplication
等同 @Configuration + @EnableAutoConfiguration + @ComponentScan


- - - -

## @RequestMapping
可以用來定義 class 或 method 的 routing URL pattren

## @Controller
定義 class 為 controller

## @RestController
算是進階的 @Controller，自動追加 @ResponseBody

範例：

```java
@RestController
@RequestMapping("/")
public class Test {
	@RequestMapping("/hello/{name}")
	String hello(@PathVariable("name") String name){
		return "Hello " + name;
	}
}
```

可以用 `my.server.com/hello/wtf` 瀏覽並得到 `Hello wtf` 的 response

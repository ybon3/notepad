># 名詞定義
- `main class`: 就是定義 main method 的 class，通常是 `Application.java`

## @EnableAutoConfiguration
讓 Spring Boot 根據 jar dependencies 自動判斷該如何 configure Spring，主要用來讓 "Starters" 妥善地運作

`@EnableAutoConfiguration` 通常用在 `main class`

## @Configuration
通常 `main class` 被定義成主要的 `@Configuration`

## @ComponentScan
掃描指定 package 下的 Component class，若無指定，就以該 class 的 package 為 root 下去掃
範例：`@ComponentScan(basePackages = "org.pac4j.springframework.web")`


## @SpringBootApplication
等同 `@Configuration` + `@EnableAutoConfiguration` + `@ComponentScan`

## @Enable* annotations 
`@EnableConfigurationProperties(Wtf.class)`

## @Import
import 其他的 configuration classes

## @ImportResource
load XML configuration filess


## @Component (@Service and @Repository extended)
使用 classpath scanning 來避免 xml bean definition

## @Bean
可搭配 `@Configuration` 

- - - -

## @RequestMapping
可以用來定義 class 或 method 的 routing URL pattren

## @Controller
定義 class 為 controller

## @RestController
算是進階的 `@Controller`，自動追加 `@ResponseBody`

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

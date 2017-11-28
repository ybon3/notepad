測試自己寫的 Java 程序
---------------------

http://tonylit.me/2015/12/10/jmeter%E6%B5%8B%E8%AF%95java/

jar import by pom.xml:

  ```xml
  <dependency>
    <groupId>org.apache.jmeter</groupId>
    <artifactId>ApacheJMeter_java</artifactId>
    <version>3.3</version>
  </dependency>
  ```


注意事項：

- 透過 GUI 可以設定每一個 `Java Request` 要執行的 class （extends `AbstractJavaSamplerClient`），
  實際上在跑的時候，設定的每一個 Thread 都會 new 一個 instance。

- Thread Group 中的迴圈其實並沒有一值產生新的 Thread，而是重複的執行 `AbstractJavaSamplerClient.runTest()`

- 不要在 `Java Request` 要執行的 class 中使用 instance scope 的 field，**似乎**會有 Thread-safe 的問題


中文引導教學
-----------

http://cloudchen.logdown.com/posts/247932/apache-jmeter-tool-for-load-test-and-measure-performance

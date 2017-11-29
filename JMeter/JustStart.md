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
  實際上在跑的時候，設定的每一個 Thread 的 `Java Request` 都會 new 一個 instance。

- Thread Group 中的迴圈其實並沒有一值產生新的 Thread，而是重複的執行 `AbstractJavaSamplerClient.runTest()`

- ~不要在 `Java Request` 要執行的 class 中使用 instance scope 的 field，**似乎**會有 Thread-safe 的問題~（這是誤會）


所以，如果我有一個測試計畫設定為 3 個執行緒、5 組 `Java Request`、跑 10 次，實際上會有：

- 每個執行緒處理 5 組 `Java Request` 
- 共產生 15 個指定 class 的 instance
- `runTest()` 總共執行 150 次


中文引導教學
-----------

http://cloudchen.logdown.com/posts/247932/apache-jmeter-tool-for-load-test-and-measure-performance

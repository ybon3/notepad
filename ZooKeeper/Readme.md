> ## 參考文件來源
> https://zookeeper.apache.org/doc/r3.4.13/zookeeperStarted.html#sc_ConnectingToZooKeeper
> Base on version 3.4.13

- [How to check if ZooKeeper is running or up from command prompt?](https://stackoverflow.com/questions/29106546/how-to-check-if-zookeeper-is-running-or-up-from-command-prompt)  

- [Hen 重要的範例](https://zookeeper.apache.org/doc/r3.4.13/javaExample.html)

- [中國同胞把上述範例中文化也頗值得參考](http://www.cnblogs.com/haippy/archive/2012/07/20/2600077.html)

- [ZooKeeper Java客户端API使用之更新数据](https://my.oschina.net/lvyi/blog/521846)

- [動手玩Windows 10 Docker(3) - 有關Docker的網絡連接](https://dotblogs.com.tw/swater111/2017/01/03/171042)

- [珍妮佛的學習筆記](https://cutejaneii.wordpress.com/2017/07/24/docker-10-%E7%94%A8docker%E5%BB%BA%E7%AB%8Bzookeeper-clustermulti-host/)

- [Zookeeper深入理解(二)(编程实践之Master-Worker)](https://t.hao0.me/zookeeper/2015/03/02/zk-program-master-worker.html)

- [ZooKeeper/FAQ](https://wiki.apache.org/hadoop/ZooKeeper/FAQ) 

- - - - - -

綜合所有 Survey，完成本篇範例，主要提供：

- 觀測（Watch）ZNode 的功能（拿來當 Event-Driven 使用）
- 變更 ZNode Data 的範例
- 建立 ZNode 的範例


至於如何建立 ZooKeeper Server 就自己想辦法啦～我是直接使用 Docker Hub 上面的版本建立在 Docker 上來玩的
<br/><br/>

**注意事項：**

- 別在 Windows OS 中使用 ToolBox + VirtualBox 來玩 Docker 掛 ZooKeeper，我不想解釋
- Not REST, use socket
- 在 Docker container 中佈署後使用
	`sudo docker exec -t zookeeper bin/zkCli.sh -server 127.0.0.1:2181`
	進入後台，但執行指令無法正確看到 output
- 可以監聽尚未存在的 `NODE`


參考指令
--------

```
echo stat | nc 172.17.0.1 8080
echo mntr | nc 172.17.0.1 8080
echo isro | nc 172.17.0.1 8080
```


Run ZooKeeper on Windows
------------------------

1. [到此下載](http://apache.stu.edu.tw/zookeeper/)
2. 解壓為目錄後，於 `X:\zookeeper-3.4.13\conf\` 中，參考 `zoo_sample.cfg` 來建立 `zoo.cfg`
3. 執行 `X:\zookeeper-3.4.13\bin\zkServer.cmd`


關於實作 Event-Driven 的心得
============================


首先了解幾件事情：

1. `ZooKeeper` 這個 class 主要就是跟 Server 連線用，有幾個情況會導致該 instance 不可再使用，如 `Expired`
2. 在 create `ZooKeeper` 的 instance 時提供的 `Watcher` 主要是針對 Server 的連線狀態做反應
3. `ZooKeeper.exists()` 是一個一次性的指令，我們大多會使用 asynchronous 類型的 method；
	可以提供一個全新的 `Watcher` implement 或者設為 true 會直接使用 create `ZooKeeper` 時提供的那一個 `Watcher`
4. 由於 `exists()` 是一個一次性的指令，當他的 callback 行為結束後，該 Node 發生的事情不會再觸發 `Watcher` 的 `process()`
5. `Watcher` 關注的是 Server 的連線狀態，我們在 `exists()` 能夠使用，從設計的角度來看似乎是設計者讓它「順便可以這樣」
6. Server 斷線（Disconnected）會**一直**自動嘗試重新連線


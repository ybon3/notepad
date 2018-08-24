> ## 參考文件來源
> https://zookeeper.apache.org/doc/r3.4.13/zookeeperStarted.html#sc_ConnectingToZooKeeper


- [How to check if ZooKeeper is running or up from command prompt?](https://stackoverflow.com/questions/29106546/how-to-check-if-zookeeper-is-running-or-up-from-command-prompt)  

- [Hen 重要的範例](https://zookeeper.apache.org/doc/r3.4.13/javaExample.html)

- [中國同胞把上述範例中文化也頗值得參考](http://www.cnblogs.com/haippy/archive/2012/07/20/2600077.html)

- [ZooKeeper Java客户端API使用之更新数据](https://my.oschina.net/lvyi/blog/521846)


- - - - - -

綜合所有 Survey，完成本篇範例，主要提供：

- 觀測（Watch）ZNode 的功能（拿來當 Event-Driven 使用）
- 變更 ZNode Data 的範例
- 建立 ZNode 的範例


至於如何建立 ZooKeeper Server 就自己想辦法啦～我是直接使用 Docker Hub 上面的版本建立在 Docker 上來玩的
<br/><br/>

**注意事項：別在 Windows OS 中使用 ToolBox + VirtualBox 來玩 Docker 掛 ZooKeeper，我不想解釋。**

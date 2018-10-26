關於如何建立 ZK Cluster
======================

- [How to setup and run multiple zookeeper instances on one box](https://www.kumarchetan.com/blog/2016/12/07/how-to-setup-and-run-multiple-zookeeper-instances-on-one-box/)
- 


## 概略說明：

1. 在 Windows 環境下必須完成 `zoo.cfg` 的設定
2. dataDir 中必須要有 `myid` 檔案，並註明 id 編號
3. 新加入的 ZooKeeper server 在 `zoo.cfg` 必須完整描述 cluster 中其他的 ZooKeeper server

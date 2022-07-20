> ## 參考文件來源
> 
> Base on version ????

- [深入浅出理解基于 Kafka 和 ZooKeeper 的分布式消息队列](https://gitbook.cn/books/5ae1e77197c22f130e67ec4e/index.html)

- [Docker – (8) 用Docker建立Kafka服務](https://cutejaneii.wordpress.com/2017/06/19/docker-7-%E7%94%A8docker%E5%BB%BA%E7%AB%8Bkafka%E6%9C%8D%E5%8B%99%E4%B8%8A/)

- [Kafka剖析（一）：Kafka背景及架构介绍](http://www.infoq.com/cn/articles/kafka-analysis-part-1)

- [API Document](https://kafka.apache.org/20/javadoc/index.html?org/apache/kafka/clients/producer/KafkaProducer.html)

- [Apache kafka 工作原理介绍](https://www.ibm.com/developerworks/cn/opensource/os-cn-kafka/index.html)

- [Kafka設計解析之Kafka背景及架構介紹](https://read01.com/aOmoA.html)

- 

- - - -

ACK 設定
--------

reference: https://kafka.apache.org/documentation/#producerconfigs


- acks=0  
If set to zero then the producer will not wait for any acknowledgment from the server at all. The record will be immediately added to the socket buffer and considered sent. No guarantee can be made that the server has received the record in this case, and the retries configuration will not take effect (as the client won't generally know of any failures). The offset given back for each record will always be set to -1.

- acks=1  
This will mean the leader will write the record to its local log but will respond without awaiting full acknowledgement from all followers. In this case should the leader fail immediately after acknowledging the record but before the followers have replicated it then the record will be lost.

- acks=all  
This means the leader will wait for the full set of in-sync replicas to acknowledge the record. This guarantees that the record will not be lost as long as at least one in-sync replica remains alive. This is the strongest available guarantee. This is equivalent to the acks=-1 setting.

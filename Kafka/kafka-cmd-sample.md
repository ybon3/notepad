Windows version
===============

```bash
./bin/windows/kafka-server-start.bat ./config/server.properties


#create topic
kafka-topics --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic steam


#delete topic
kafka-topics --delete --zookeeper eshop-ms.eastus.cloudapp.azure.com:2182 --topic steam


#producer
kafka-console-producer --broker-list localhost:9092 --topic steam
kafka-console-producer --broker-list eshop-ms.eastus.cloudapp.azure.com:9092 --topic test


#consumer
kafka-console-consumer --bootstrap-server localhost:9092 --topic steam
kafka-console-consumer --bootstrap-server eshop-ms.eastus.cloudapp.azure.com:9092 --topic test

#show all message
kafka-console-consumer --bootstrap-server eshop-ms.eastus.cloudapp.azure.com:9092 --topic steam --from-beginning
```

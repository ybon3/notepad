> Reference: https://zookeeper.apache.org/doc/r3.4.13/zookeeperProgrammers.html


Terms
=====

- path = node = znode 有時候因情況使用不同的詞
<br/><br/>

## `ez.exists()` 是一次性的命令（One-time trigger）

- 參數 `watch` 會影響指定 `path` 下一次的事件是否會被 watcher 注意到，
  如果參數是 boolean 值，就使用預設的 watcher（也就是 ZooKeeper constructor 提供的那一個），
  也可以指定全新的 watcher

- **通常** node listen 的情況大概會是：

  1. invoke exists() 會先取得 node 現狀，並指明 watcher 必須 listen 下一次事件
  2. node 發生事件，觸發 watcher 的 process()，屬於 Node EventType 就 call exists()
  3. exists() 執行 processResult()，處理事件，並指名 watcher 必須 listen 下一次事件
  
  上述 2-3 重複循環
  
 

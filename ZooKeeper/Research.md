> Reference: https://zookeeper.apache.org/doc/r3.4.13/zookeeperProgrammers.html


Terms
=====

- path = node = znode 有時候因情況使用不同的詞
<br/><br/>


`ez.exists()` 是一次性的命令（One-time trigger）
----------------------------------------------

- 參數 `watch` 會影響指定 `path` 下一次的事件是否會被 watcher 注意到，
  如果參數是 boolean 值，就使用預設的 watcher（也就是 ZooKeeper constructor 提供的那一個），
  也可以指定全新的 watcher

- **通常** node listen 的情況大概會是：

  1. invoke exists() 會先取得 node 現狀，並指明 watcher 必須 listen 下一次事件
  2. node 發生事件，觸發 watcher 的 process()，屬於 Node EventType 就 call exists()
  3. exists() 執行 processResult()，處理事件，並指名 watcher 必須 listen 下一次事件
  
  上述 2-3 重複循環
<br/><br/>


KeeperState
-----------

參閱：https://zookeeper.apache.org/doc/r3.4.13/api/index.html

來自 `Watcher.process(WatchedEvent event)` 的 `event.getState()`

- ### SyncConnected: The client is in the connected state 
	最正常的連接狀態

- ### Expired: The serving cluster has expired this session
	ClientCnxn 不會嘗試重新連接，會關閉 socket，**ZooKeeper 的 instance 也不可再使用**
	
	要製造這個現象可以把網路斷開一段時間（看 server timeout 的設定）再恢復連線 
	
- ### Disconnected: The client is in the disconnected state
	ClientCnxn 會一直嘗試重新連接，而且當這個狀態發生時，
	`ZooKeeper.getState().isConnected()` 還不見得會變成 `false`

- ### AuthFailed / ConnectedReadOnly / SaslAuthenticated
	尚未使用到
<br/><br/>


漏接事件的風險
-------------

根據 Watches 的機制，理論上會發生；當 NodeChange 發生（以下簡稱**事件**）時，程序順序理論上是：  

`Watcher -> process() -> exists() -> processResult()`

從事件發生直到 invoke `exists()` 之間存在著 latency，
若在這段時間（即使可能很短）中又發生別的事件，那麼可能就會漏接該次事件。


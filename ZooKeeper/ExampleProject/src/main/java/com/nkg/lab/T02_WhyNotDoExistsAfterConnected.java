package com.nkg.lab;

import java.io.IOException;

import org.apache.zookeeper.AsyncCallback.StatCallback;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.KeeperException.Code;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.common.Context;

/**
 * 先說結論：找不太到在還沒確認完成連線就先執行 zk.exists() 的理由 ...
 *
 * 照官方範例的寫法，如果一開始沒連上 server 就會一直重複執行 zk.exists()，
 * 應該是蠻吃資源的；不過如果連上之後再斷就沒差。
 */
public class T02_WhyNotDoExistsAfterConnected implements Watcher, StatCallback {
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	private ZooKeeper zk;
	private String path;

	public T02_WhyNotDoExistsAfterConnected(String hostPort, String znode) throws KeeperException, IOException {
		zk = new ZooKeeper(hostPort, 3000, this);
		path = znode;
	}

	@Override
	public void process(WatchedEvent event) {
		logger.info("process() " + event.getType() + " | " + event.getState());

		if (event.getType() == EventType.None) {
			switch (event.getState()) {
			case SyncConnected:
				zk.exists(path, true, this, null);
				break;
			case Expired:
				break;
			default:
				break;
			}
		} else {
			zk.exists(path, true, this, null);
		}
	}

	@Override
	public void processResult(int rc, String path, Object ctx, Stat stat) {
		logger.info("processResult() " + Code.get(rc));
		logger.info("processResult() Version " + (stat != null ? stat.getVersion() : "null"));

		switch (Code.get(rc)) {
		case OK:
		case NONODE:
			break;
		case SESSIONEXPIRED:
		case NOAUTH:
			return;
		default:
			zk.exists(path, true, this, null);
		}
	}

	//---------------------------------------------------------------------------------
	public static void main(String[] args) throws InterruptedException, IOException {
		try {
			new T02_WhyNotDoExistsAfterConnected(Context.HOST, Context.NODE).run();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void run() {
		try {
			synchronized (this) {
				while (true) {
					wait();
					System.out.println("awake...");
				}
			}
		} catch (InterruptedException e) {}
	}
}

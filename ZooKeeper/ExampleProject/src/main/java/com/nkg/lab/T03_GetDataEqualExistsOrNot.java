package com.nkg.lab;

import java.io.IOException;

import org.apache.zookeeper.AsyncCallback.DataCallback;
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
 *
 */
public class T03_GetDataEqualExistsOrNot
		implements Watcher, DataCallback {
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	private ZooKeeper zk;
	private String path;

	public T03_GetDataEqualExistsOrNot(String hostPort, String znode) throws KeeperException, IOException {
		zk = new ZooKeeper(hostPort, 3000, this);
		path = znode;
	}

	@Override
	public void process(WatchedEvent event) {
		logger.info("process() " + event.getType() + " | " + event.getState());

		if (event.getType() == EventType.None) {
			switch (event.getState()) {
			case SyncConnected:
				zk.getData(path, true, this, null);
				break;
			case Expired:
				break;
			default:
				break;
			}
		} else {
			zk.getData(path, true, this, null);
		}
	}

	@Override
	public void processResult(int rc, String path, Object ctx, byte[] data, Stat stat) {
		logger.info("processResult() " + Code.get(rc) +
				" Version " + (stat != null ? stat.getVersion() : "null"));
		showData(data);

		switch (Code.get(rc)) {
		case OK:
		case NONODE:
			break;
		case SESSIONEXPIRED:
		case NOAUTH:
			return;
		default:
			zk.getData(path, true, this, null);
		}
	}

	public void showData(byte[] data) {
		try {
			System.out.println("=========");
			System.out.write(data);
			System.out.println();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	//---------------------------------------------------------------------------------
	public static void main(String[] args) throws InterruptedException, IOException {
		try {
			new T03_GetDataEqualExistsOrNot(Context.HOST, Context.NODE).run();
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

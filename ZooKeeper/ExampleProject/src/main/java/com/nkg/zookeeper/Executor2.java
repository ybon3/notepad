package com.nkg.zookeeper;

import java.io.IOException;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.KeeperException.Code;

/**
 * Just Watch event
 */
public class Executor2 implements Watcher, Runnable {
	private static String filename = "D:\\testZookeeper.txt";
	private static String HOST = "localhost:2181";
	private static String NODE = "/";

	String znode;

	DataMonitor dm;

	ZooKeeper zk;

	public Executor2(String hostPort, String znode) throws KeeperException, IOException {
		this.znode = znode;
		zk = new ZooKeeper(hostPort, 3000, this);
	}

	public static void main(String[] args) {
		try {
			new Executor2(HOST, NODE).run();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void process(WatchedEvent event) {
		System.out.println("process " + event.getType() + " | " + event.getState());
		String path = event.getPath();
		if (event.getType() == Event.EventType.None) {
			switch (event.getState()) {
			case SyncConnected:
				break;

			case Expired:
				closing(Code.SESSIONEXPIRED);
				break;

			case Disconnected:
				break;

			case AuthFailed:
			case ConnectedReadOnly:
			case SaslAuthenticated:
			default:
				break;
			}
			switch (event.getState()) {
			case SyncConnected:
				break;
			case Expired:
				break;
			default:
				break;
			}
		} else {
			if (path != null && path.equals(znode)) {
				// Something has changed on the node, let's find out
				try {
					zk.exists(znode, this);
				} catch (KeeperException | InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public void run() {
		try {
			synchronized (this) {
				System.out.println("NODE: [" + znode + "] | " + filename);
				wait();
				System.out.println("done.");
			}
		} catch (InterruptedException e) {}
	}

	public void closing(Code rc) {
		System.out.println("zookeeper " + zk.getState().isAlive());
		System.out.println("closing() " + rc);
		synchronized (this) {
			notifyAll();
		}
	}
}
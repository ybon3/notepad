package com.nkg.zookeeper;

import java.io.FileOutputStream;
import java.io.IOException;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.KeeperException.Code;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

public class Executor implements Watcher, Runnable, DataMonitor.DataMonitorListener {
	private static String filename = "D:\\testZookeeper.txt";
	private static String HOST = "localhost:2181";
	private static String NODE = "/";

	String znode;

	DataMonitor dm;

	ZooKeeper zk;

	public Executor(String hostPort, String znode) throws KeeperException, IOException {
		this.znode = znode;
		zk = new ZooKeeper(hostPort, 3000, this);
		dm = new DataMonitor(zk, znode, null, this);
	}

	public static void main(String[] args) {
		try {
			new Executor(HOST, NODE).run();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/***************************************************************************
	 * We do process any events ourselves, we just need to forward them on.
	 *
	 * @see org.apache.zookeeper.Watcher#process(org.apache.zookeeper.proto.WatcherEvent)
	 */
	@Override
	public void process(WatchedEvent event) {
		dm.process(event);
	}

	@Override
	public void run() {
		try {
			synchronized (this) {
				System.out.println("NODE: [" + znode + "] | " + filename);
				while (!dm.dead) {
					wait();
					System.out.println("awake...");
				}
			}
		} catch (InterruptedException e) {
		}
	}

	@Override
	public void closing(Code rc) {
		System.out.println("closing() " + rc);
		synchronized (this) {
			notifyAll();
		}
	}

	@Override
	public void exists(byte[] data) {
		if (data != null) {
			try {
				FileOutputStream fos = new FileOutputStream(filename);
				fos.write(data);
				fos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

			System.out.println("we can react something here ...");
		}
	}
}
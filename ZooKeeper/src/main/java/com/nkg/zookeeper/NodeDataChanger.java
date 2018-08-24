package com.nkg.zookeeper;

import java.io.IOException;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

public class NodeDataChanger implements Watcher, Runnable {
	private ZooKeeper zk;
	private String changeStringTo = "Taiwan No.7";

	public NodeDataChanger() throws IOException, KeeperException, InterruptedException {
		zk = new ZooKeeper(Context.HOST, 3000, this);
		byte[] data = zk.getData(Context.ZNODE, false, null);
		System.out.println(">>> Change data from:");
		System.out.println(new String(data));
		System.out.println(">>> to");
		System.out.println(changeStringTo);
		zk.setData(Context.ZNODE, changeStringTo.getBytes(), -1);
	}

	@Override
	public void process(WatchedEvent event) {
		System.out.println("process() " + event.getType() + " | " + event.getState());
//		if (event.getType() == EventType.None) {
//			// We are are being told that the state of the
//			// connection has changed
//			switch (event.getState()) {
//			case SyncConnected:
//				int ver;
//				try {
//					ver = zk.exists(Context.ZNODE, null).getVersion();
//					System.out.println("Current version: " + ver);
//					byte[] data = changeStringTo.getBytes();
//
//					/*
//					 * Set the data for the node of the given path
//					 * if such a node exists and the given version matches the version of the node
//					 * (if the given version is -1, it matches any node's versions).
//					 */
//					Stat stat = zk.setData(Context.ZNODE, data, -1);
//					System.out.println(stat.getCzxid()+","+stat.getMzxid()+","+stat.getVersion());
//
//					Stat stat2 = zk.setData(Context.ZNODE, data, stat.getVersion());
//					System.out.println(stat2.getCzxid()+","+stat2.getMzxid()+","+stat2.getVersion());
//
//				} catch (KeeperException | InterruptedException e) {
//					e.printStackTrace();
//				}
//
//				System.exit(0);
//			default:
//				break;
//			}
//		}
	}

	// -------------------------------------------------
	public static void main(String[] args) {
		try {
			new NodeDataChanger();//.run();
		} catch (Exception e) {
			e.printStackTrace();
		};
	}

	@Override
	public void run() {
		try {
			synchronized (this) {
				while (true) {
					wait();
				}
			}
		} catch (InterruptedException e) {}
	}
}

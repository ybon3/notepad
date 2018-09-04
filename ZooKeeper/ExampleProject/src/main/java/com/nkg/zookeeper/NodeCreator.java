package com.nkg.zookeeper;

import java.io.IOException;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

public class NodeCreator implements Watcher {
	private static String HOST = "localhost:2181";
	private static String NEW_NODE = "/HONE_TEST/YYYY";

	private ZooKeeper zk;

	public NodeCreator() throws IOException, KeeperException, InterruptedException {
		zk = new ZooKeeper(HOST, 3000, this);

		Stat stat = zk.exists(NEW_NODE, this);
		System.out.println("NODE ["+NEW_NODE+"] exist: " + (stat != null));
		if (stat == null) {
			Util.createPath(zk, NEW_NODE);
		}

		stat = zk.exists(NEW_NODE, this);
		System.out.println("Iinital version: " + stat.getVersion());
	}

	@Override
	public void process(WatchedEvent event) {
		System.out.println("process " + event.getType() + " | " + event.getState());
	}

	// -------------------------------------------------
	public static void main(String[] args) {
		try {
			new NodeCreator();
		} catch (Exception e) {
			e.printStackTrace();
		};
	}
}

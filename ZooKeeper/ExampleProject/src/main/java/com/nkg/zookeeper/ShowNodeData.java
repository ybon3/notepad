package com.nkg.zookeeper;

import java.io.IOException;

import org.apache.zookeeper.ZooKeeper;

public class ShowNodeData {
	private static String HOST = "localhost:2181";
	private static String NODE = "/";

	public static void main(String[] args) throws IOException {
		ZooKeeper zk = new ZooKeeper(HOST, 3000, new SimpleWatcher());
		byte[] b = Util.getNodeData(zk, NODE);
		System.out.println("byte(s): " + b.length);
		System.out.println(new String(b));
	}
}

package com.nkg.zookeeper;

import java.io.IOException;
import java.util.List;

import org.apache.zookeeper.ZooKeeper;

public class ShowAllNode {
	private static String HOST = "localhost:2181";
	private static String NODE = "/";

	public static void main(String[] args) throws IOException {
		ZooKeeper zk = new ZooKeeper(HOST, 3000, new SimpleWatcher());
		List<String> list = Util.getAllChild(zk, NODE);

		for (String path : list) {
			System.out.println(path);
		}
	}
}

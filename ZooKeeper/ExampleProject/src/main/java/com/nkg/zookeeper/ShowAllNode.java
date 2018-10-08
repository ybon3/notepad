package com.nkg.zookeeper;

import java.io.IOException;
import java.util.List;

import org.apache.zookeeper.ZooKeeper;

import com.common.Context;

public class ShowAllNode {
	private static String HOST = Context.HOST;
	private static String NODE = Context.NODE;

	public static void main(String[] args) throws IOException {
		ZooKeeper zk = new ZooKeeper(HOST, 3000, new SimpleWatcher());
		List<String> list = Util.getAllChild(zk, NODE);

		for (String path : list) {
			System.out.println(path);
		}
	}
}

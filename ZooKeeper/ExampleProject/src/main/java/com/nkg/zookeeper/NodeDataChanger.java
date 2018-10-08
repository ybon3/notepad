package com.nkg.zookeeper;

import java.io.IOException;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooKeeper;

import com.common.Context;

public class NodeDataChanger {
	private static String HOST = Context.HOST;
	private static String NODE = Context.NODE;
	private static String changeStringTo = "1234567890";

	public static void main(String[] args)
			throws IOException, KeeperException, InterruptedException {
		ZooKeeper zk = new ZooKeeper(HOST, 3000, new SimpleWatcher());
		byte[] data = zk.getData(NODE, false, null);
		System.out.println(">>> Change data from:");
		System.out.println(data == null ? "[null]" : new String(data));
		System.out.println(">>> to");
		System.out.println(changeStringTo);
		zk.setData(NODE, changeStringTo.getBytes(), -1);
	}
}

package com.nkg.zookeeper;

import java.io.IOException;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooKeeper;

import com.common.Context;

public class NodeDataAppender {
	private static String HOST = Context.HOST;
	private static String NODE = Context.NODE;
	private static String appendString = "QQQQQQQQQQQQQQQQQQQQQQ";

	public static void main(String[] args) throws IOException, KeeperException, InterruptedException {
		ZooKeeper zk = new ZooKeeper(HOST, 3000, new SimpleWatcher());

		String dst = appendString;
		byte[] data = zk.getData(NODE, false, null);
		if (data != null && data.length > 0) {
			dst = new String(data) + "\r\n" + appendString;
		}

		System.out.println("append result: ");
		System.out.println(dst);
		zk.setData(NODE, dst.getBytes(), -1);
	}
}

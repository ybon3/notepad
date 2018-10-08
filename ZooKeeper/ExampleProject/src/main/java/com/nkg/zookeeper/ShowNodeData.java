package com.nkg.zookeeper;

import java.io.IOException;

import org.apache.zookeeper.ZooKeeper;

import com.common.Context;

public class ShowNodeData {
	private static String HOST = Context.HOST;
	private static String NODE = Context.NODE;

	public static void main(String[] args) throws IOException {
		ZooKeeper zk = new ZooKeeper(HOST, 3000, new SimpleWatcher());
		byte[] b = Util.getNodeData(zk, NODE);
		System.out.println("byte(s): " + b.length);
		System.out.println(new String(b));
	}
}

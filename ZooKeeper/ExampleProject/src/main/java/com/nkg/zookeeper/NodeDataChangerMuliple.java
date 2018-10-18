package com.nkg.zookeeper;

import java.io.IOException;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooKeeper;

import com.common.Context;

/**
 * 用來連續發動事件，可以看到 client 如何漏接事件
 */
public class NodeDataChangerMuliple {
	private static String HOST = Context.HOST;
	private static String NODE = Context.NODE;
	private static String changeStringTo = "1234567890";
	private static int count = 100;

	public static void main(String[] args)
			throws IOException, KeeperException, InterruptedException {
		ZooKeeper zk = new ZooKeeper(HOST, 3000, new SimpleWatcher());
		byte[] data = zk.getData(NODE, false, null);
		byte[] newData = changeStringTo.getBytes();
		for (int i = 0; i < count; i++) {
			zk.setData(NODE, newData, -1);
		}
		System.out.println(">>> Change data from:");
		System.out.println(data == null ? "[null]" : new String(data));
		System.out.println(">>> to");
		System.out.println(changeStringTo);
		System.out.println("Run " + count + " times done.");
	}
}

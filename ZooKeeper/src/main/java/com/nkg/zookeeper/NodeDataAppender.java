package com.nkg.zookeeper;

import java.io.IOException;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

public class NodeDataAppender implements Watcher {
	private ZooKeeper zk;
	private String appendString = "Taiwan No.1111";

	public NodeDataAppender() throws IOException, KeeperException, InterruptedException {
		zk = new ZooKeeper(Context.HOST, 3000, this);
		String src = new String(zk.getData(Context.ZNODE, false, null));
		String dst = append(src, appendString, true);

		System.out.println("append result: ");
		System.out.println(dst);
		zk.setData(Context.ZNODE, dst.getBytes(), -1);
	}

	private String append(String src, String newStr, boolean ln) {
		return src + (ln ? "\r\n" : "") + newStr;
	}

	// -------------------------------------------------
	public static void main(String[] args) {
		try {
			new NodeDataAppender();
		} catch (Exception e) {
			e.printStackTrace();
		};
	}

	@Override
	public void process(WatchedEvent event) {
		System.out.println("process() " + event.getType() + " | " + event.getState());
	}
}

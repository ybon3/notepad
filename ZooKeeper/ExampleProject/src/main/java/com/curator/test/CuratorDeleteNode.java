package com.curator.test;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryNTimes;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.common.Context;

public class CuratorDeleteNode {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	private static String HOST = Context.HOST;
	private static String NODE = "/ESHOP/LOCAL";

	public CuratorDeleteNode() throws Exception {
		CuratorFramework client = CuratorFrameworkFactory.newClient(
			HOST,
			new RetryNTimes(3, 5000)
		);
		client.start();
		logger.info("zk client start successfully!");

		Stat stat = client.checkExists().forPath(NODE);
		if (stat != null) {
//			logger.info("delete " + client.delete().forPath(NODE));
			logger.info("delete " + client.delete().deletingChildrenIfNeeded().forPath(NODE));
		} else {
			logger.info("NODE: " + NODE + " is not exists");
		}
	}

	public static void main(String[] args) {
		try {
			new CuratorDeleteNode();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

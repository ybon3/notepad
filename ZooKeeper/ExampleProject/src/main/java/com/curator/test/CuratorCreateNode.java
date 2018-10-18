package com.curator.test;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryNTimes;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.common.Context;

public class CuratorCreateNode {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	private static String HOST = Context.HOST;
	private static String NODE = Context.NODE;

	public CuratorCreateNode() throws Exception {
		CuratorFramework client = CuratorFrameworkFactory.newClient(
			HOST,
			new RetryNTimes(3, 5000)
		);
		client.start();
		logger.info("zk client start successfully!");

		Stat stat = client.checkExists().forPath(NODE);
		if (stat == null) {
//			logger.info("create " + client.create().forPath(NODE));
			logger.info("create " + client.create().creatingParentsIfNeeded().forPath(NODE));
		} else {
			logger.info("NODE: " + NODE + " exists");
		}
	}

	public static void main(String[] args) {
		try {
			new CuratorCreateNode();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

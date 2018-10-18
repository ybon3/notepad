package com.curator.test;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryNTimes;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.common.Context;

public class CuratorCheckNodeExist {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	private static String HOST = Context.HOST;
	private static String NODE = "/XXXXX";

	public CuratorCheckNodeExist() throws Exception {
		CuratorFramework client = CuratorFrameworkFactory.newClient(
			HOST,
			new RetryNTimes(3, 5000)
		);
		client.start();
		logger.info("zk client start successfully!");

		Stat stat = client.checkExists().forPath(NODE);

		logger.info("stat " + (stat == null ? "null" : stat.getCversion()));
	}

	public static void main(String[] args) {
		try {
			new CuratorCheckNodeExist();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

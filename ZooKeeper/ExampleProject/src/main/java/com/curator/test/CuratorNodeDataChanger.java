package com.curator.test;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryNTimes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.common.Context;

public class CuratorNodeDataChanger {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	private static String HOST = Context.HOST;
	private static String NODE = Context.NODE;
	private static String changeStringTo = "1234567890";

	public CuratorNodeDataChanger() throws Exception {
		CuratorFramework client = CuratorFrameworkFactory.newClient(
			HOST,
			new RetryNTimes(3, 5000)
		);
		client.start();
		logger.info("zk client start successfully!");

		client.setData().forPath(NODE, changeStringTo.getBytes());
		client.close();
	}

	public static void main(String[] args) {
		try {
			new CuratorNodeDataChanger();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

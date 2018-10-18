package com.curator.test;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.NodeCacheListener;
import org.apache.curator.retry.RetryNTimes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.common.Context;

public class CuratorClientTest implements Runnable {
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	public CuratorClientTest() throws Exception {
		CuratorFramework client = CuratorFrameworkFactory.newClient(
			Context.HOST,
			new RetryNTimes(3, 5000)
		);
		client.start();
		logger.info("zk client start successfully!");

		NodeCache watcher = new NodeCache(
				client,
				Context.NODE,
				false	// if cache data
		);

		watcher.getListenable().addListener(new NodeCacheListener(){
			@Override
			public void nodeChanged() throws Exception {
				ChildData data = watcher.getCurrentData();
				logger.info("Receive event: "
						+ "path=[" + data.getPath() + "]"
						+ ", data=[" + new String(data.getData()) + "]"
						+ ", version=[" + data.getStat().getVersion() + "]");
			}
		});

		watcher.start();//.start(StartMode.BUILD_INITIAL_CACHE);
		logger.info("Register zk watcher successfully!");
	}

	public static void main(String[] args) {
		try {
			new CuratorClientTest().run();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		try {
			synchronized (this) {
				wait();
			}
		} catch (InterruptedException e) {}
	}
}

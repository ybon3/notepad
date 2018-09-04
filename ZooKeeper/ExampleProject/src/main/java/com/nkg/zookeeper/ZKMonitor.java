package com.nkg.zookeeper;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.ZooKeeper.States;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ZKMonitor {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	private long delay = 5000;

	public ZKMonitor(ZooKeeper zk) {

		while(true) {
			States stat = zk.getState();
			logger.info("isAlive: " + stat.isAlive() + " | isConnected: " + stat.isConnected());

			try {
				Thread.sleep(delay);
				if (!stat.isConnected()) {
					if (stat.isAlive()) {
						zk.close();
					} else {
						zk.exists("/", true);
					}
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (KeeperException e) {
				e.printStackTrace();
			}
		}
	}
}

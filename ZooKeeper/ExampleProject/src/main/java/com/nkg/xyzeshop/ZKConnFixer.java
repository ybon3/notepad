package com.nkg.xyzeshop;

import org.apache.zookeeper.ZooKeeper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * To prevent that sometime ZooKeeper server on docker container
 * should be connect success, but not.
 * @author Dante
 */
@Deprecated
public class ZKConnFixer {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	private int initDelay = 3000;
	private int interval = 3000;
	private int countLimit = 10;
	private ZooKeeper zk;
	private boolean working = false;

	public ZKConnFixer(ZooKeeper zk, int initDelay) {
		this.zk = zk;
		this.initDelay = initDelay;
	}

	public ZKConnFixer(ZooKeeper zk) {
		this.zk = zk;
	}

	public int getInterval() {
		return interval;
	}

	public void setInterval(int interval) {
		this.interval = interval;
	}

	public int getCountLimit() {
		return countLimit;
	}

	public void setCountLimit(int countLimit) {
		this.countLimit = countLimit;
	}

	/**
	 * Cyclically check given ZooKeeper instance connection status
	 * @return
	 * <ul>
	 * 	<li>null is working
	 * 	<li>true re-connect succeed
	 * 	<li>false re-connect too much times
	 * </ul>
	 */
	public Boolean intervalCheck() {
		synchronized (this) {
			logger.info("working: " + working);
			if (working) { return null; }
			working = true;
		}

		delay(initDelay); //Since ZooKeeper.getState().isConnected() not change immediately ...
		logger.info("intervalCheck() started");

		for (int i = 0; i < countLimit; i++) {
			if (zk.getState().isConnected()) {
				working = false;
				return true;
			}

			delay(interval);
		}

		working = false;
		return false;
	}

	private void delay(int millisec) {
		try {
			Thread.sleep(millisec);
		} catch (InterruptedException e) {}
	}

	public void close() {
		countLimit = -1;
		interval = 1;
	}
}

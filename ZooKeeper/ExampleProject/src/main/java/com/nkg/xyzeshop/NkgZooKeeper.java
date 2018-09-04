package com.nkg.xyzeshop;

import java.io.IOException;

import org.apache.zookeeper.AsyncCallback.StatCallback;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.KeeperException.Code;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.newKinPo.util.ZooKeeperUtil;

/**
 * {@link ZooKeeper wrapper}
 * @author Dante
 */
@Deprecated
public class NkgZooKeeper implements Watcher, StatCallback {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	private ZooKeeper zk;
	private Watcher watcher;
	private StatCallback statCallback;

	private int initDelay = 3000;
	private int interval = 2000;
	private int countLimit = 5;
	private boolean working = false;

	public NkgZooKeeper(String connectString, int sessionTimeout, Watcher watcher) throws IOException {
		this.zk = new ZooKeeper(connectString, sessionTimeout, this);
		this.watcher = watcher;
	}

	public void exists(String path, boolean watch, StatCallback cb, Object ctx) {
		zk.exists(path, watch, this, ctx);
		this.statCallback = cb;
	}

	public byte[] getData(String path, boolean watch, Stat stat) throws KeeperException, InterruptedException {
		return zk.getData(path, watch, stat);
	}

	public Stat setData(String path, byte[] data) throws KeeperException, InterruptedException {
		return zk.setData(path, data, -1);
	}

	@Override
	public void process(WatchedEvent event) {

		if (!zk.getState().isAlive()) { return; }

		if (event.getType() == EventType.None &&
			event.getState() == KeeperState.Disconnected) {

			Boolean isConnected = intervalCheck();

			if (isConnected == null) { return; } //working

			if (!isConnected) {
				try {
					zk.close();
				} catch (InterruptedException e) {}
				onClose();
				return;
			}
		}

		watcher.process(event);
	}

	@Override
	public void processResult(int rc, String path, Object ctx, Stat stat) {
		Code code = Code.get(rc);
		switch (code) {
		case NONODE:
			createPath(path);
			break;
		case CONNECTIONLOSS:
			if (!zk.getState().isAlive()) { return; }
		default:
			break;
		}

		statCallback.processResult(rc, path, ctx, stat);
	}

	public void onClose() {
		logger.info("Try to Re-connect timeout, close ZooKeeper ...");
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
	private Boolean intervalCheck() {
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

	private void createPath(String path) {
		boolean isCreated = ZooKeeperUtil.createPath(zk, path);
		logger.info("createPath() " + isCreated);
		if (!isCreated) {
			logger.error("Could not create path: {}", path);
		}
	}

	private static void delay(int millisec) {
		try {
			Thread.sleep(millisec);
		} catch (InterruptedException e) {}
	}
}

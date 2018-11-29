package com.nkg.lab;

import java.io.IOException;

import org.apache.zookeeper.AsyncCallback.StatCallback;
import org.apache.zookeeper.KeeperException.Code;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.common.Context;

/**
 * 證明 zk.exists() 其實只會 run 一次
 *
 * 參數 watch 會影響 processResult() 執行之後 watcher 是否會對非 Node 變化產生反應
 * 若 true，下次 Node 變化會 call process()，反之則否。
 */
public class T01_ProveExistsJustRunOnce implements Watcher, StatCallback {
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	private ZooKeeper zk;
	private String path;

	public T01_ProveExistsJustRunOnce(String hostPort, String znode) throws KeeperException, IOException {
		zk = new ZooKeeper(hostPort, 6000, this);
		path = znode;

		//無論是否有正確連接到 ZK Server 都會執行
		zk.exists(path, true, this, null);
	}

	@Override
	public void process(WatchedEvent event) {
		logger.info("process() " + event.getType() + " | " + event.getState());
	}

	@Override
	public void processResult(int rc, String path, Object ctx, Stat stat) {
		logger.info("processResult() " + Code.get(rc));
		logger.info("processResult() Version " + (stat != null ? stat.getVersion() : "null"));

		//如果 zk server 一開始連不上，又不加上這一行，
		//就會導致連上 server 之後不會觸發 processResult()
		//doSomething(Code.get(rc));
	}

	private void doSomething(Code code) {
		switch (code) {
		case OK:
		case NONODE:
			break;
		case SESSIONEXPIRED:
		case NOAUTH:
			return;
		default:
			zk.exists(path, true, this, null);
		}
	}

	//---------------------------------------------------------------------------------
	public static void main(String[] args) throws InterruptedException, IOException {
		try {
			new T01_ProveExistsJustRunOnce(Context.HOST, Context.NODE).run();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void run() {
		try {
			synchronized (this) {
				while (true) {
					wait();
					System.out.println("awake...");
				}
			}
		} catch (InterruptedException e) {}
	}
}

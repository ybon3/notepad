package com.nkg.zookeeper;

import java.io.IOException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.KeeperException.Code;

import com.common.Context;

public class Executor3 implements Watcher, Runnable {
	private static String HOST = Context.HOST;
	private static String NODE = Context.NODE;

	private ZooKeeper zk;

	public Executor3() {
		connect();
		//show(zk, NODE);
	}

	private void connect() {
		try {
			zk = new ZooKeeper(HOST, 3000, this);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// -------------------------------------------------
	public static void main(String[] args) {
		try {
			new Executor3().run();
		} catch (Exception e) {
			e.printStackTrace();
		};
	}

	@Override
	public void run() {
		try {
			synchronized (this) {
				while (true) {
					wait();
				}
			}
		} catch (InterruptedException e) {}
	}

	@Override
	public void process(WatchedEvent event) {
		System.out.println("process() " + event.getType() + " | " + event.getState());
		if (event.getType() == Event.EventType.None) {
			switch (event.getState()) {
			case SyncConnected:
				System.out.println("size: " + Util.getAllChild(zk, NODE).size());
				break;
			case Expired:
				closing(Code.SESSIONEXPIRED);
				break;
			default:
				break;
			}
		}
	}

	public void closing(Code rc) {
		System.out.println("closing() " + rc);
		synchronized (this) {
			connect();
		}
	}
}

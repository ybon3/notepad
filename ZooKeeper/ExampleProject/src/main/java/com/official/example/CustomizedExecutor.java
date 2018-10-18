package com.official.example;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import com.common.Context;

public class CustomizedExecutor
	implements Watcher, Runnable, CustomizedDataMonitor.DataMonitorListener
{
	String znode;

	CustomizedDataMonitor dm;

	ZooKeeper zk;

	public CustomizedExecutor(String hostPort, String znode) throws KeeperException, IOException {
		zk = new ZooKeeper(hostPort, 3000, this);
		dm = new CustomizedDataMonitor(zk, znode, null, this);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			new CustomizedExecutor(Context.HOST, Context.NODE).run();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/***************************************************************************
	 * We do process any events ourselves, we just need to forward them on.
	 *
	 * @see org.apache.zookeeper.Watcher#process(org.apache.zookeeper.proto.WatcherEvent)
	 */
	@Override
	public void process(WatchedEvent event) {
		dm.process(event);
	}

	@Override
	public void run() {
		try {
			synchronized (this) {
				while (!dm.dead) {
					wait();
				}
			}
		} catch (InterruptedException e) {
		}
	}

	@Override
	public void closing(int rc) {
		synchronized (this) {
			notifyAll();
		}
	}

	static class StreamWriter extends Thread {
		OutputStream os;

		InputStream is;

		StreamWriter(InputStream is, OutputStream os) {
			this.is = is;
			this.os = os;
			start();
		}

		@Override
		public void run() {
			byte b[] = new byte[80];
			int rc;
			try {
				while ((rc = is.read(b)) > 0) {
					os.write(b, 0, rc);
				}
			} catch (IOException e) {
			}

		}
	}

	@Override
	public void exists(byte[] data) {
		try {
			System.out.println("=========");
			System.out.write(data);
			System.out.println();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
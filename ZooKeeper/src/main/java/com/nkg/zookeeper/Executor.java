package com.nkg.zookeeper;

import java.io.FileOutputStream;
import java.io.IOException;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.KeeperException.Code;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

public class Executor implements Watcher, Runnable, DataMonitor.DataMonitorListener {
	private static String filename = "D:\\testZookeeper2.txt";

	String znode;

	DataMonitor dm;

	ZooKeeper zk;

	Process child;

	public Executor(String hostPort, String znode) throws KeeperException, IOException {
		zk = new ZooKeeper(hostPort, 3000, this);
		dm = new DataMonitor(zk, znode, null, this);
	}

	public static void main(String[] args) {
		try {
			new Executor(Context.HOST, Context.ZNODE).run();
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
					System.out.println("awake...");
				}
			}
		} catch (InterruptedException e) {
		}
	}

	@Override
	public void closing(Code rc) {
		synchronized (this) {
			notifyAll();
		}
	}

	@Override
	public void exists(byte[] data) {
		if (data == null) {
			if (child != null) {
				System.out.println("Killing process");
				child.destroy();
				try {
					child.waitFor();
				} catch (InterruptedException e) {
				}
			}
			child = null;
		} else {
			if (child != null) {
				System.out.println("Stopping child");
				child.destroy();
				try {
					child.waitFor();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			try {
				FileOutputStream fos = new FileOutputStream(filename);
				fos.write(data);
				fos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

			System.out.println("we can react something here ...");
		}
	}
}
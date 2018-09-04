package com.nkg.xyzeshop;

import com.newKinPo.util.CacheCoordinator;

public class Tester {
	public static void main(String[] args) throws InterruptedException {
		System.setProperty(CacheCoordinator.SYSTEM_PROP_ZOOKEEPER_HOST, "localhost:2181");
		System.setProperty(CacheCoordinator.SYSTEM_PROP_ZOOKEEPER_NODE, "/ESHOP/STAGE/XYZ/CACHE");

		CacheCoordinator instance = CacheCoordinator.getInstance();

		new Tester().run();
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

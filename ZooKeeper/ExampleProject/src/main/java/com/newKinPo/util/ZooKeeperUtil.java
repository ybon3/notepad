package com.newKinPo.util;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

public class ZooKeeperUtil {
	public static boolean createPath(ZooKeeper zk, String path) {
		if (path.equals("/")) {
			throw new IllegalArgumentException("Cannot create path: " + path);
		}

		String[] nodes = path.split("/");

		try {
			String c_path = "";
			for (int i = 1; i < nodes.length; i++) {
				c_path += "/" + nodes[i];
				Stat stat = zk.exists(c_path, false);

				if (stat != null) { continue; } //exists, pass

				zk.create(c_path, null, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
			}
		} catch (KeeperException | InterruptedException e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}
}

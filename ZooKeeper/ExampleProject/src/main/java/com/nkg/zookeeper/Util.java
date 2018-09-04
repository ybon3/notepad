package com.nkg.zookeeper;

import java.util.ArrayList;
import java.util.List;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

public class Util {
	public static List<String> getAllChild(ZooKeeper zk, String path) {
		List<String> list = new ArrayList<>();

		try {
			Stat stat = zk.exists(path, false);
			if (stat == null) { return list; }

			list.add(path);
			list.addAll(getChildren(zk, path));
		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;
	}

	private static List<String> getChildren(ZooKeeper zk, String path) throws KeeperException, InterruptedException {
		List<String> list = new ArrayList<>();

		List<String> childs = zk.getChildren(path, false);

		if (!path.endsWith("/")) { path += "/"; }

		for (String childPath : childs) {
			childPath = path + childPath;
			list.add(childPath);
			list.addAll(getChildren(zk, childPath));
		}

		return list;
	}

	/**
	 * @return true for exists or create success
	 */
	public static boolean createNode(ZooKeeper zk, String path) {
		try {
			Stat stat = zk.exists(path, false);
			if (stat == null) {
				zk.create(path, null, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
			}
			return true;
		} catch (KeeperException | InterruptedException e) {
			e.printStackTrace();
		}

		return false;
	}

	public static byte[] getNodeData(ZooKeeper zk, String path) {
		try {
			Stat stat = zk.exists(path, false);
			if (stat != null) {
				return zk.getData(path, false, null);
			}
		} catch (KeeperException | InterruptedException e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * @return true for exists or create success
	 */
	public static boolean createPath(ZooKeeper zk, String path) {
		if (path.equals("/")) {
			throw new IllegalArgumentException("Cannot create path: " + path);
		}

		try {
			Stat stat = zk.exists(path, false);
			if (stat != null) { return true; } //exists

			// start creation
			String[] nodes = path.split("/");
			StringBuilder sb = new StringBuilder();
			for (int i = 1; i < nodes.length; i++) {
				sb.append("/").append(nodes[i]);

				String c_path = sb.toString();
				stat = zk.exists(sb.toString(), false);

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

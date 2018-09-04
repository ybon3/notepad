package com.nkg.xyzeshop;

import java.io.IOException;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooKeeper;

import com.google.gson.Gson;
import com.newKinPo.object.ObjCacheSync;
import com.nkg.zookeeper.SimpleWatcher;

public class NkgNodeDataChanger {
	private static String HOST = "xxxx:8080";
	private static String NODE = "/ESHOP/STAGE/XYZ/CACHE";

	public static void main(String[] args)
			throws IOException, KeeperException, InterruptedException {
		ObjCacheSync cacheData = new ObjCacheSync();
		cacheData.countryId = 1;
		cacheData.cacheArgs = new String[]{"CATEGORYSALEITEM"};
		cacheData.dateTime = System.currentTimeMillis();
		String changeStringTo = new Gson().toJson(cacheData);

		ZooKeeper zk = new ZooKeeper(HOST, 3000, new SimpleWatcher());
		byte[] data = zk.getData(NODE, false, null);
		System.out.println(">>> Change data from:");
		System.out.println(data == null ? "[null]" : new String(data));
		System.out.println(">>> to");
		System.out.println(changeStringTo);
		zk.setData(NODE, changeStringTo.getBytes(), -1);
	}
}

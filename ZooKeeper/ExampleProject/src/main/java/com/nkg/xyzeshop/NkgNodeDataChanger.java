package com.nkg.xyzeshop;

import java.io.IOException;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooKeeper;

import com.common.Context;
import com.google.gson.Gson;
import com.newKinPo.object.ObjCacheSync;
import com.nkg.zookeeper.SimpleWatcher;

public class NkgNodeDataChanger {
	private static String HOST = Context.HOST;
	private static String NODE = Context.NODE;

	public static void main(String[] args)
			throws IOException, KeeperException, InterruptedException {
		ObjCacheSync cacheData = new ObjCacheSync();
		cacheData.countryId = 2;
		cacheData.cacheArgs = new String[]{"PRODUCTPAGE"};
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

package com.newKinPo.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.zookeeper.AsyncCallback.StatCallback;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.KeeperException.Code;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nkg.xyzeshop.NkgZooKeeper;

/**
 * Coordination caches on different WebServer instance,
 * through ZooKeeper implement it.
 *
 * @author Dante
 */
public class CacheCoordinator implements Watcher, StatCallback {
	public static final String SYSTEM_PROP_ZOOKEEPER_HOST = "zookeeper-host"; //for standalone.xml
	public static final String SYSTEM_PROP_ZOOKEEPER_NODE = "zookeeper-node"; //for standalone.xml

	//Singleton
	private static CacheCoordinator instance = new CacheCoordinator();
	public static CacheCoordinator getInstance() { return instance; }

	private Logger logger = LoggerFactory.getLogger(this.getClass());
	private String zk_host;
	private String watch_node;

	private NkgZooKeeper nkg_zk;
	private byte prevData[];
	private List<NodeDataChangedListener> listenerList = new ArrayList<>();
	private boolean actived;

	private CacheCoordinator() {
		zk_host = System.getProperty(SYSTEM_PROP_ZOOKEEPER_HOST);
		if (StringUtils.isEmpty(zk_host)) {
			logger.warn("Couldn't find system-properties ["+SYSTEM_PROP_ZOOKEEPER_HOST+"] in standalone.xml");
			return;
		}

		watch_node = System.getProperty(SYSTEM_PROP_ZOOKEEPER_NODE);
		if (StringUtils.isEmpty(watch_node)) {
			logger.warn("Couldn't find system-properties ["+SYSTEM_PROP_ZOOKEEPER_NODE+"] in standalone.xml");
			return;
		}

		connect();
	}

	private void connect() {
		logger.info("try connect ...");
		try {
			nkg_zk = new NkgZooKeeper(zk_host, 3000, this) {
				@Override
				public void onClose() {
					super.onClose();
					closing(Code.SYSTEMERROR);
				}
			};
			nkg_zk.exists(watch_node, true, this, null);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void process(WatchedEvent event) {
		logger.info("process() " + event.getType() + " | " + event.getState());
		if (event.getType() == EventType.None) {
			switch (event.getState()) {
			case SyncConnected:
				actived = true;
				break;
			case Expired:
				closing(Code.SESSIONEXPIRED);
				break;
			case Disconnected:
				break;
			case SaslAuthenticated:
			case AuthFailed:
			case ConnectedReadOnly:
			default:
				break;
			}
		} else {
			//Could be one of EventType:
			//NodeChildrenChanged / NodeCreated / NodeDataChanged / NodeDeleted
			String path = event.getPath();
			if (path != null && path.equals(watch_node)) {
				// Something has changed on the node, let's find out
				nkg_zk.exists(watch_node, true, this, null);
			}
		}
	}

	@Override
	public void processResult(int rc, String path, Object ctx, Stat stat) {
		logger.info("processResult " + Code.get(rc));
		Code code = Code.get(rc);
		switch (code) {
		case OK:
			readData();
			break;
		case NONODE:
			break;
		case SESSIONEXPIRED:
		case NOAUTH:
			closing(code);
			return;
		default:
			// Retry errors
			nkg_zk.exists(watch_node, true, this, null);
			return;
		}
	}

	private void readData() {
		byte b[] = null;
		try {
			b = nkg_zk.getData(watch_node, false, null);
		} catch (KeeperException | InterruptedException e) {
			// We don't need to worry about recovering now.
			// The watch callbacks will kick off any exception handling
			logger.error("readData()", e);
		}

		logger.debug("readData() \r\n{}", (b == null ? "null" : new String(b)));
		if ((b == null && b != prevData)
				|| (b != null && !Arrays.equals(prevData, b))) {
			prevData = b;
			for (NodeDataChangedListener listener : listenerList) {
				try {
					listener.dataChanged(b);
				} catch (Exception e) {}
			}
		}
	}

	private void closing(Code rc) {
		logger.error("closing() " + rc);
		actived = false;
		connect();
	}

	public void setNodeData(String data) {
		try {
			nkg_zk.setData(watch_node, data.getBytes());
		} catch (KeeperException | InterruptedException e) {
			logger.error("setNodeData()", e);
		}
	}

	public boolean isActived() {
		return actived;
	}

	//------------------------------------------------------------------
	public static void addListener(NodeDataChangedListener listener) {
		instance.listenerList.add(listener);
	}

	public static boolean removeListener(NodeDataChangedListener listener) {
		return instance.listenerList.remove(listener);
	}

	public static interface NodeDataChangedListener {
		public void dataChanged(byte[] data);
	}
}

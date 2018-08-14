Synchronized use object(this) as key
------------------------------------

### Case 1: one instance on same method

全部都要等，就像排隊等單一窗口處理事情一樣。
<br/><br/>


### Case 2: one instance on diff method

全部都要等，雖然窗口有兩個，但只准一個窗口在單一時間做事。
<br/><br/>


### Case 3: two instance on same method

不用等，就像有兩個人在處理分別（或同性質）的窗口事務。
<br/><br/>


### Case 4: two instance on same method, use same key

要等，雖然事務員有多個，但只有一個服務窗口。
<br/><br/>


- - - -

### 參考用的程式碼

```java
package com.nkg.sync;

import java.util.Random;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class SyncTest_LV2 {

	static Random r = new Random();
	static Executor executor = Executors.newCachedThreadPool();

	public static void main(String[] args) {
//		case01(2);
//		case02(2);
//		case03(2);
		case04(2);
	}

	static void case01(int sec) {
		SyncTest_LV2 ins = new SyncTest_LV2();

		executor.execute(new Task("foo-1") {
			@Override
			public void exec() {
				ins.foo(sec);
			}
		});
		executor.execute(new Task("foo-2") {
			@Override
			public void exec() {
				ins.foo(sec);
			}
		});
	}

	static void case02(int sec) {
		SyncTest_LV2 ins = new SyncTest_LV2();

		executor.execute(new Task("foo") {
			@Override
			public void exec() {
				ins.foo(sec);
			}
		});
		executor.execute(new Task("bar") {
			@Override
			public void exec() {
				ins.bar(sec);
			}
		});
	}

	static void case03(int sec) {
		SyncTest_LV2 ins01 = new SyncTest_LV2();
		SyncTest_LV2 ins02 = new SyncTest_LV2();

		executor.execute(new Task("foo-ins01") {
			@Override
			public void exec() {
				ins01.foo(sec);
			}
		});
		executor.execute(new Task("foo-ins02") {
			@Override
			public void exec() {
				ins02.foo(sec);
			}
		});
	}

	static void case04(int sec) {
		SyncTest_LV2 ins01 = new SyncTest_LV2();
		SyncTest_LV2 ins02 = new SyncTest_LV2();

		executor.execute(new Task("useSameKey-ins01") {
			@Override
			public void exec() {
				ins01.useSameKey(sec);
			}
		});
		executor.execute(new Task("useSameKey-ins02") {
			@Override
			public void exec() {
				ins02.useSameKey(sec);
			}
		});
	}

	void foo(int sec) {
		synchronized (this) {
			sleep("foo", sec);
		}
	}

	void bar(int sec) {
		synchronized (this) {
			sleep("bar", sec);
		}
	}

	void useSameKey(int sec) {
		synchronized (r) {
			sleep("useSameKey", sec);
		}
	}

	public static void sleep(String name, int sec) {
		System.out.println(name + " delay " + sec);
		try {
			Thread.sleep(sec * 1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private static abstract class Task implements Runnable {
		String id;
		Task(String id) {
			this.id = id;
		}

		@Override
		public void run() {
			System.out.println(id + " ready");
			exec();
			System.out.println(id + " done.");
		}

		abstract public void exec();
	}
}
```

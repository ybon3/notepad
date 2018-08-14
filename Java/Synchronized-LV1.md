> # 常常忘記，所以好好地記錄一下
<br/><br/>

Synchronized at Method
----------------------

### Case 1: one static method

全部都要等，就像排隊等單一窗口處理事情一樣。
<br/><br/>


### Case 2: separete static method

全部都要等，雖然窗口有兩個，但只准一個窗口在單一時間做事。
<br/><br/>


### Case 3: one instance one method

全部都要等，跟 Case 1 一樣道理。
<br/><br/>


### Case 4: one instance separete method

全部都要等，跟 Case 2 一樣道理。
<br/><br/>


### Case 5: two instance same method

不用等。
<br/><br/>


- - - -

### 參考用的程式碼

```java
package com.nkg.sync;

import java.util.Random;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class SyncTest_LV1 {

	static Random r = new Random();
	static Executor executor = Executors.newCachedThreadPool();

	public static void main(String[] args) {
//		case01(2);
//		case02(2);
//		case03(2);
//		case04(2);
		case05(2);
	}

	static void case01(int sec) {
		executor.execute(new Task("foo-1") {
			@Override
			public void exec() {
				foo(sec);
			}
		});
		executor.execute(new Task("foo-2") {
			@Override
			public void exec() {
				foo(sec);
			}
		});
	}

	static void case02(int sec) {
		executor.execute(new Task("foo") {
			@Override
			public void exec() {
				foo(sec);
			}
		});
		executor.execute(new Task("bar") {
			@Override
			public void exec() {
				bar(sec);
			}
		});
	}

	static void case03(int sec) {
		SyncTest_LV1 t = new SyncTest_LV1();
		executor.execute(new Task("insFoo-1") {
			@Override
			public void exec() {
				t.insFoo(sec);
			}
		});
		executor.execute(new Task("insFoo-2") {
			@Override
			public void exec() {
				t.insFoo(sec);
			}
		});
	}

	static void case04(int sec) {
		SyncTest_LV1 t = new SyncTest_LV1();
		executor.execute(new Task("insFoo") {
			@Override
			public void exec() {
				t.insFoo(sec);
			}
		});
		executor.execute(new Task("insBar") {
			@Override
			public void exec() {
				t.insBar(sec);
			}
		});
	}

	static void case05(int sec) {
		SyncTest_LV1 t1 = new SyncTest_LV1();
		SyncTest_LV1 t2 = new SyncTest_LV1();
		executor.execute(new Task("insFoo-1") {
			@Override
			public void exec() {
				t1.insFoo(sec);
			}
		});
		executor.execute(new Task("insFoo-2") {
			@Override
			public void exec() {
				t2.insFoo(sec);
			}
		});
	}


	synchronized static void foo(int sec) {
		sleep("foo", sec);
	}

	synchronized static void bar(int sec) {
		sleep("bar", sec);
	}

	synchronized void insFoo(int sec) {
		sleep("insFoo", sec);
	}

	synchronized void insBar(int sec) {
		sleep("insBar", sec);
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

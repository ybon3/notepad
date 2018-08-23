> # 記錄測試心得
> 版本：9.1.7.final


前情提要
-------

在單一 JVM 下，起數個 Thread 分別扮演「更新資料」及「取得資料」的角色，並在隨機的時間（1~10 秒間）進行動作。  

此模型主要模擬原本 XYZ WebService 的 `MemCachceUtil`、`CacheController` 的行為。  

原本的測試目的主要是想觀察 `removeCache()` 的必要性以及其帶來的影響，但在實驗過程中卻意外發生更多的錯誤，顯示出 infinispan 的種種特性。


錯誤情況
-------

- ### ISPN000453: Attempt to define configuration for cache CACHE_NAME which already exists

當對同一個 cache name 進行 `EmbeddedCacheManager.defineConfiguration()` 時，越密集地 invoke 越容易發生，
推測是 `EmbeddedCacheManager.defineConfiguration()` 用 Thread 的方式 implement 所導致。  
<br/><br/>


- ### ISPN000323: Cache 'CACHE_NAME' is in 'STOPPING' state and this is an invocation not belonging ...

取得的 cache 因為在別的地方被 invoke `removeCache()`，所以其實狀態已經變成無法使用。  
<br/><br/>


- ### ISPN000323: Cache 'CACHE_NAME' is in 'TERMINATED' state and so it does not accept new invocations.

基本原因同 `ISPN000323`  
<br/><br/>


- ### ISPN000436: Cache 'CACHE_NAME' has been requested, but no cache configuration exists with that name and no default cache has been set for this container

invoke `getCache()` 之前必須先確認是否存在（透過 `cacheExists()`）。
然而，在 Multi-thread 的情況下，若沒有用 synchronized lock 住，這樣的確認方式也沒有意義。  
<br/><br/>


目前最保險的做法
--------------

應避免使用 `removeCache()`；做為替代應使用：

```java
public void clearCache() {
if (cacheManager.cacheExists(CACHE_NAME)) {
	cacheManager.getCache(CACHE_NAME).clear();
}
```


結論
====

1. 如果要使用 `removeCache()` 請撤底考慮到所有的 Thread-Safe 問題；

2. 當連續執行下列程式碼時本身存在著：

```java
if (!cacheManager.cacheExists(CACHE_NAME)) {
	ConfigurationBuilder config = new ConfigurationBuilder();
	config.expiration().lifespan(10, TimeUnit.SECONDS);
	cacheManager.defineConfiguration(CACHE_NAME, config.build());
}
```

卻會炸 ISPN000453 的不合理錯誤：`cacheExists()` 回傳 `false`，卻又在 `defineConfiguration()` 說已經存在

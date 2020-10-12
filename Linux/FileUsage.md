磁碟使用量
=========

```linux
df -h 
```


各資料夾使用量
============

reference: https://blog.xuite.net/cadmus.lin/yo/39567921-%E3%80%90%E7%B3%BB%E3%80%91%E5%AF%9F%E7%9C%8B%E6%AF%8F%E5%80%8B%E7%9B%AE%E9%8C%84%E4%BD%94%E7%94%A8%E5%AE%B9%E9%87%8F%E7%9A%84%E5%A4%A7%E5%B0%8F

範例：查詢指定資料夾的總佔用空間

```linux
du -sh ./apigateway/ ./order/
```

範例：查詢指定路徑之下所有資料夾的總佔用空間

```linux
du -sh ./volumes/*
```

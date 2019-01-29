關於測試報告
===========

### ab / abs 的 POST 功能對於 Windows 的支援度非常差

### Failed requests 的涵義：

- Connect：無法送出要求、目標主機連接失敗、要求的過程中連線被中斷
- Length：回應的內容長度不一致 ( 以 Content-Length 標頭值為判斷依據 )
- Exception：發生無法預期的錯誤

第一次發出 HTTP request 與後續發出的 HTTP request 所得到回應的 HTML 長度都是不同大小的（每次回應的 Content-Length 大小不一致）時候，
就會算在 Failed requests 的 Length 問題的失敗，因此這類 Length 不一致的失敗在進行「動態網頁」壓力測試時是合理的，可以不予理會。


範例命令
-------

```console
# 同時發出 100 個 request，總共發出 1000 個
ab -c 100 -n 1000 -k "https://localhost:8080/api/b2b/order?merchant_id=eshop-stress-02&region=TWN"

# 使用 PUT 方法
ab -c 1 -n 1 -k -T "application/json" -u "./b2b.json" -H "content-type: application/json" "https://localhost:8080/mock"

# 使用 POST 方法
ab -c 1 -n 1 -k -T "application/json" -p "./b2b.json" -H "content-type: application/json" "https://localhost:8080/mock"
```

Reference
---------

- [對於 ApacheBench 進行測試時出現的 Failed requests 詳解](https://blog.miniasp.com/post/2009/10/07/Explain-ApacheBench-ab-for-the-Failed-request-field.aspx)
- [使用 ApacheBench 進行網站的壓力測試](https://blog.miniasp.com/post/2008/06/30/Using-ApacheBench-ab-to-to-Web-stress-test.aspx)
- [網頁伺服器監測站！！不能不知道的ApacheBench使用方法](https://blog.hellosanta.com.tw/%E7%B6%B2%E7%AB%99%E8%A8%AD%E8%A8%88/%E4%BC%BA%E6%9C%8D%E5%99%A8/%E7%B6%B2%E9%A0%81%E4%BC%BA%E6%9C%8D%E5%99%A8%E7%9B%A3%E6%B8%AC%E7%AB%99%EF%BC%81%EF%BC%81%E4%B8%8D%E8%83%BD%E4%B8%8D%E7%9F%A5%E9%81%93%E7%9A%84apachebench%E4%BD%BF%E7%94%A8%E6%96%B9%E6%B3%95)
- [压力测试工具ab的两个Time per request 参数含义和区别](https://www.imooc.com/article/19952)

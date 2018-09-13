HiMirror Login Problem
======================

## 錯誤訊息：`ERR_TOO_MANY_REDIRECTS`

問題說明：

1. xyzSecure 認定 request 未授權，導到 OAuth 
2. 於 OAuth Server 完成登入，與 OAuth 建立起 session
3. OAuth 將 user 重導回 xyzSecure 提供的 uri 並帶著需要的 Token 等資訊
4. xyzSecure 解析失敗 **（`Crypto.decryptAES()` 解密變成亂碼）** ，將連線又導回 OAuth
5. 由於 user 已於 OAuth 完成登入並建立起 session 所以回到 3.

然後開始 3 4 5 一直循環，最後噴 `ERR_TOO_MANY_REDIRECTS`


1. 與 XYZ Printing 不同的是，HiMirror 是使用 OAuth，所以只有 HiMirror 的版本才會發生
2. 已確認不是專案 source code 的問題
3. 推測是 OS 版本的問題，我使用的是英文版的 Windows 7，已經交叉確認在中文版 Windows7 以及 Mac 上運行都沒有這個問題
4. 承上，雖然幾乎可確認 IDE 的問題，但也許 OS 的版本間接導致 IDE build 才發生此問題


## Workaround:

就在解密之後的程式碼中，將應該正確解碼的內容寫死在程式中




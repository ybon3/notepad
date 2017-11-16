Reference: https://stackoverflow.com/questions/4223059/eclipse-secure-storage

依序進行以下步驟（Windows 7）：

1. 關閉 Eclipse
2. 刪除 `C:\Users\[帳號]\.eclipse\org.eclipse.equinox.security`
3. 建立文字檔 `C:\Users\[帳號]\.eclipse\master`，檔案內容為登入系統的密碼
4. 在 `eclipse.ini` 的第一行加上：
  
    ```
    -eclipse.password
    /home/user/.eclipse/master
    ```
    注意：兩行**不要**合併成一行
    
5. 啟動 Eclipse

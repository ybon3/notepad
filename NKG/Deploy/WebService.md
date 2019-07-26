XYZ
---

有兩台，要請 OP 人員協助將 Balance 改為其中一台，並對另一台進行更新

1. 通報相關人員關閉分流
2. 觸發 Jenkin CI/CD (by push branch)
3. 確認更新完畢
4. 通報相關人員切換分流
5. 變更 Jenkin deploy 對象設定
6. 觸發 Jenkin CI/CD (by manual)
7. 確認更新完畢
8. 請相關人員恢復分流


確認辦法
=======

1. WinSCP 確認 war 檔版本(日期)
2. call /XYZEShop/HELLO 



錯誤處理
=======

- Jenkis compile 完成，但上傳 war 檔失敗：

  ```
  SSH: Connecting from host [backend-agent]
  SSH: Connecting with configuration [eShop Web Service Server #1] ...
  ERROR: Exception when publishing, exception message [Failed to connect and initialize SSH connection. Message: [Failed to connect session for config [eShop Web Service Server #1]. Message [java.net.ConnectException: Connection timed out (Connection timed out)]]]
  Build step 'Send files or execute commands over SSH' changed build result to UNSTABLE
  Finished: UNSTABLE
  ```
  
  此為 Jenkin 無法與機器透過 SSH 上傳 War 檔的錯誤

**Jenkin 有提供測試連線工具：Jenkins -> 管理 Jenkins -> 設定系統 -> Publish over SSH**

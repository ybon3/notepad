> 這也是在「為 Fhir Server 加上 Interception 機制」工作項目中發現的

- Fhir Server 會在新增 / 修改時檢查 Reference 是否真的存在（就像是資料庫的 foreign key），如果不存在就跳錯誤；
  也會刪除資料時檢查是否被 Reference，如果尚被其他 Resource reference 也會跳錯誤。
  
  如果要關閉（預設是開啟），在 CLI 啟動 server 時可以加上 `--disable-referential-integrity` 設定，
  或者透過 `ca.uhn.fhir.jpa.dao.DaoConfig` 設定

- Fhir Server 對於 Reference 的檢查允許提供外部的 Reference（such as: `http://192.168.100.106:8013/baseDstu2/Device/1`）

  如果要設定為允許（預設是不允許），在 CLI 啟動 server 時可以加上 `--allow-external-refs` 設定，
  或者透過 `ca.uhn.fhir.jpa.dao.DaoConfig` 設定

> CLI 意為 CommandLine Interface

Base Repo: https://github.com/jamesagnew/hapi-fhir

其中的 `hapi-fhir-cli` 提供指令啟動 Fhir Server 的功能


Repo 內包含兩個 Module:

- **hapi-fhir-cli-app**：主要提供 CLI 指令，包含 command 及 option，比較重要的是 `ca.uhn.fhir.cli.RunServerCommand`

  這個 Module 用程式化的方式啟動 Jetty Web Server，並手動處理了 `web.xml` 的一部分工作；
  hapi-fhir-cli-jpaserver 的 `web.xml` 被 mark 掉的內容都在這邊用程式完成了，目的是為了實現用 CLI 進行 Dstu2、Dstu3 的切換

- **hapi-fhir-cli-jpaserver**：內含使用 Derby 資料庫的 Restful Fhir Server（jpaserver）以及存取 Fhir Server 的 WebUI（Tester）

  - jpaserver：對應 JpaServerDemo
  - Tester：由 `spring` 的 `AnnotationConfigWebApplicationContext` 處理，參考 `web.xml`

  內含的幾個檔案用途：
  
  - FhirDbConfig：資料庫設定
  - FhirServerConfig：for Dstu2 的 server 設定
  - FhirServerConfigDstu3：for Dstu3 的 server 設定
  - FhirTesterConfig：for Web UI（就是那個 Hapi 的網頁存取介面）的設定


Build
-----

`hapi-fhir-cli-app` 中包含了 `hapi-fhir-cli-jpaserver`，所以 jpaserver 有修正，就要兩邊都重新執行 `mvn install`，
或者可以直接在 `hapi-fhir-cli` 執行 `mvn install`



Interceptor
-----------

Reference: http://hapifhir.io/doc_rest_server_interceptor.html

從上述連結的文件可以了解 `IServerInterceptor` 各個 method 的生命期

在 https://github.com/DatacomRD/hapi-fhir/tree/v2.5-interceptor/hapi-fhir-cli 的修正中則使用了
`ServerOperationInterceptorAdapter` 提供的介面，原因是：

1. 對於 Restful Fhir Server 而言，新增**可以是** `CREATE` or `UPDATE` operation（原因不在此贅述）
  使用 `ServerOperationInterceptorAdapter.resourceCreated()` 比起透過 `RestOperationTypeEnum` 來判斷相對來說比較可靠
  
1. 對於未提供 ID 的 `CREATE` operation 來說，`IServerInterceptor` 只能取得原本 POST 的資料，無法取得 server 自動產生的 ID，
  但 `ServerOperationInterceptorAdapter.resourceCreated()` 傳入的 `IBaseResource` 不但有包含 ID，還已經 parse 成 `IBaseResource`

另外要注意的是：

- `ServerOperationInterceptorAdapter.resourceCreated()` 中發生任何錯誤（non-catched Exception or Error）
  該次新增的行為會被取消（或者說中斷）

- 雖然可以在 `ServerOperationInterceptorAdapter.resourceCreated()` 中透過傳入的 `IBaseResource` 取得自動產生的 ID，
  但實質上在 `resourceCreated()` 完成之前，該筆資料並非真正寫入到 Fhir Server，如果在這個時間點就將該 ID 寫入到任何 Reference 就會發生錯誤，
  進而導致 `resourceCreated()` 中斷



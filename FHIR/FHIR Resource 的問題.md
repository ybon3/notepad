> 版本：v1.3

問題描述（Since 2016-12-15）
--------------------------

始自於：在使用 gwtRepo 透過 identifier 查詢 DiagnosticReport 時，取回資料回傳了：
** No narrative available - Error: Could not parse as expression: \"Untitled Diagnostic Report\" (diagnosticreport:15) **

伺服器的 log 也出現大量的錯誤訊息，主要為：

- ERROR c.u.f.n.BaseThymeleafNarrativeGenerator - Failed to generate narrative


已經確認的事項：

- 對 `FooGwtRepo` 來說，是因為回傳的資料不是預期的 xml 格式而導致錯誤
- 跟 [DomainResource.text](http://hl7.org/fhir/domainresource-definitions.html#DomainResource.text) 這個 [Narrative](http://hl7.org/fhir/narrative.html#Narrative) 型態有關，如果新增時沒有給值（不論是什麼值），產生出來的資料在單筆讀取時會只顯示 `DomainResource.text` 的內容，而非預期的 xml
- 新增 Resource 時，是否有把那些必須有值的欄位給值，跟這個問題似乎有關
- 目前的 workaround：在 DxReport 新增時提供 `text.status` 的值（甚麼值好像不影響），就算 `DomainResource.text` 還是顯示 `No narrative available ...` 但運作會正常，不會導致資料回傳時的錯誤
- 要讓 `DomainResource.text` 顯示正常，目前確認是必須要給 `code` 欄位的值


2017-11-16 再次面對這個問題造成的困擾
-----------------------------------

現象是：使用 Hapi 介面查詢時會因為 `DiagnosticReport.text` 的內容為 `No narrative available ...` 
導致 FHIR console 處理過久（每一個都跳 Exception）而 Timeout，所以介面會無法正確顯示。

目前只有 192.168.111.5 會發生，嚴格來說每一個佈署的環境都會發生，只是 192.168.111.5 處理比較慢導致 Timeout 才讓問題浮上檯面。

根本的原因在於：新增 `DiagnosticReport` 時，FHIR 會自動取 `DiagnosticReport.code.text` 來產生 
`DiagnosticReport.text` 的內容，當 `DiagnosticReport.code.text` 沒有給值就會發生上述的問題。

補充說明：儘管手動寫入了 `DiagnosticReport.text` 的內容，在取出 `DiagnosticReport` 時仍然會因為
`DiagnosticReport.code.text` 沒有給值而在 console 跳出錯誤。

而且這不是 WebUI（Tester）的錯誤而是 JpaServer 在噴的，確認是底層的問題。


Solution
--------

所以就乖乖塞值給 `DiagnosticReport.code.text` 就是了 ...


最後，已經確認 v2.5 版不會有這個問題

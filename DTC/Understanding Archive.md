main() --> StoreService(AeInfo, ArchiveService)
-----------------------------------------------

`StoreService` 負責監聽指定的 SCP，將 Store 進來的 Dicom 交由 ArchiveService 處理


ArchiveService --> FlowManager 
------------------------------

`ArchiveService` 將 Dicom 暫存並定義一系列的工作內容來決定如何處理 dicom

- 啟動時會先檢查 Income Folder，如果有待處理的 dicom 就直接丟給 `FlowManager`
- 將 Store 進來的 Dicom 暫存到 Income Folder，並交給 `FlowManager` 等待處理
- 定義要做哪些 Task 以利 `FlowManager` 進行


FlowManager --> Workflow(File)
--------------------------------

- 建立一個固定（可在 constructor 指定）大小 ThreadPool 的 `Executor`
- 啟動一個固定時間間隔的程序，檢查哪些 Dicom 要處理，滿足條件的就讓 `Executor` 用前面定義好的 Task 來處理


Workflow
--------

將檔案內容轉成 `Attributes` 讓定義好的 `Task` 一個個處理，`Task` 之間可以靠 `getData()`、`setData()` 來傳遞參數

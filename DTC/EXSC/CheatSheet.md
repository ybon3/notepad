報到工作站
=========

畫面資料 -> `HuojiawaDao.queryExamOrder()`

報到 -> `CheckInService.checkIn(List<ExamOrder>)` 
主要分兩個階段：

- 第一階段，替所有檢查項目找到適合的空位
  - 先尋找是否有院外預約對應的 `Appointment`，如果有就以該筆預約的時間為優先，
    如果找不到（若遲到也會找不到）就當成一般情況處理。
  - 將檢查項目依照儀器類別分批處理：
    - 找出儀器類別的所有設備的空位（slot）
    - 這些空位是否能夠滿足檢查項目的數量
    
- 第二階段，資料寫入作業
  - 取得 Patient，若不存在就建立
  - 計算 AccessionNumber
  - 產生 worklist（寫入 MWL）
  - 更新火箭蛙的 view 的狀態為 "C"
  - 新增 / 更新 `Appointment`
  
  
資料寫入
-------

**Fhir - Patient**
- id: examOrder.patId
- name: examOrder.patName
- gender: examOrder.sex
- birthDate: examOrder.birthday
- active: true


**Miobox**
- `mioboxDao.insertMwl(examOrder, aptment, accessionNumber, aet);`
- `accessionNumber = examOrder.reqId`


**Huojiawa**
- `huojiawaDao.updatePacsRequest(examOrder.getReqId(), "C");`


**Fhir - Appointment**
- 新增或更新
- start、end、minutesDuration: 預約時間
- status: "arrived"
- participant[0].actor: patient（前面建立的）
- identifier['accessionNumber']: examOrder.getReqId()
- identifier['Creator']: "EXSC"
- identifier['modality']: 科別
- identifier['block']: 時段
- identifier['examination']: examOrder.chargingId

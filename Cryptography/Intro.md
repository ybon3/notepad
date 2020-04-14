演算法分類及用途
================

### 雜湊函式

- 是一種單向加密算法
- 不可逆
- 用來驗證消息完整性（訊息摘要）
- MD5、SHA 家族、HMAC


### 對稱式加密演算法

- 加密與解密用同一個密鑰
- DES、AES、3DES、RC4


### 非對稱式加密演算法

- 以一組成對的公鑰與私鑰分別進行加解密來達到不同用途
- 無法透過公鑰推算出私鑰
- 加密傳輸：發送端以公鑰加密，接收端以私鑰解密，接收端負責發佈公鑰
- 數位簽章：簽章端以私鑰加密，驗證端以公鑰解密，公證第三方負責發佈公、私鑰
- RSA、DSA、ECC



RSA 非對稱加密算法
==================



MD5 訊息摘要演算法
==================

https://zh.wikipedia.org/wiki/MD5#%E7%AE%97%E6%B3%95

**MD5 = Message-Digest Algorithm**

已被證實存在弱點（1996）

輸出範例（128 bits = 16 Bytes = 32 Hex char）：`35454B055CC325EA1AF2126E27707052`


SHA 家族
========

https://zh.wikipedia.org/wiki/SHA%E5%AE%B6%E6%97%8F

**SHA = Secure Hash Algorithm**

- SHA-0 (1993)：SHA-1前身  
  
- SHA-1 (1995)：已被攻破（By CWI & Google, 2017）  
  輸出範例（160 bits = 20 Bytes = 40 Hex char）：`D2F05B425E4D869FD963F32200F7117D69A98A0F`
  
- SHA-2 (2001)：SHA-224、SHA-256、SHA-384、SHA-512、SHA-512/224、SHA-512/256  
  SHA-224 輸出範例（224 bits = 28 Bytes = 56 Hex char）：`C9A9F647FC6C60AF455A78F943D5E3213B9108B72F052B339B3223FD`  
  SHA-256 輸出範例（256 bits = 32 Bytes = 64 Hex char）：`E35089B2D968D2C00562279DD210847F3E156CAA7C9AFFBAA45A25C6C0E75EDF`  
  SHA-384 輸出範例（384 bits = 48 Bytes = 96 Hex char）：`7CFF761BFDF6C94B511E5FB8DCCBA10487B13CD29CD99177CC9A01F9505422979D28DB66004DCC572270C799841EC360`  
  SHA-512 輸出範例（512 bits = 64 Bytes = 128 Hex char）：`9B9FAB23AFD71E2FA950A4220EDD9D5F0E8CDE4C79720AE184428759CD77EB8EF69170D9710E0A0BF6BE6D4BBFFDD35119820C860EFDB89A4A05306636302D09`  
  
- SHA-3 (2015)：未普遍使用

使用 AES/CTR/NoPadding 加解密時發生的問題
=======================================


最終發現肇因來自於 Java 版本不同導致 

`Cipher.getMaxAllowedKeyLength(algorithm);`

回傳值可能為 `Integer.MAX_VALUE` 或者 128 （亦可能出現 256 或其他值）

回傳值為 `Integer.MAX_VALUE` 的版本：
- Java8-171-b11
- Java8-191 

回傳值為 128 的版本：
- Java8-77

詳細的測試程式可以參考 `AesTest.java`、`AesTest2.java`

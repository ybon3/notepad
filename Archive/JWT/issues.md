[Build Secure User Interfaces Using JSON Web Tokens (JWTs)](https://stormpath.com/blog/build-secure-user-interfaces-using-jwts)

[JJWT – JSON Web Token for Java and Android](https://stormpath.com/blog/jjwt-how-it-works-why)

[7 Best Practices for JSON Web Tokens](https://dev.to/neilmadden/7-best-practices-for-json-web-tokens)


## JWT 夾帶敏感性資料的議題 

- JWT 不關心 payload 的加密，只關注驗證
- JWT 假定傳輸過程是安全的 (secure + httpsOnly)，無法由 js & http 讀取
- JWT 本就不應該夾帶敏感資料

> reference

[If you can decode JWT how are they secure?](https://stackoverflow.com/questions/27301557/if-you-can-decode-jwt-how-are-they-secure)

[Is it safe to store sensitive data in JWT Payload?](https://stackoverflow.com/questions/43496821/is-it-safe-to-store-sensitive-data-in-jwt-payload)

Search key word: `jwt token sensitive info` `jwt token is secure`


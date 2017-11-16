環境及名詞說明
==============

slash = / （就是斜線...）

Webapps = 出事的網站應用系統

OIDC = OpenID Connect

OIDC Server：[MITRE] 提供，使用 1.2.7-SNAPSHOT 版

Chrome 版本：55.0.2883.87 m (64-bit)

Tomcat 版本：apache-tomcat-8.0.30-windows-x64

OS 環境：Windows 10 64bit


問題描述
========

Webapps 使用 OIDC 來完成身分驗證與授權，當尚未登入 OIDC 時進入 Webapps 時會重導到 OIDC 的登入頁面，
若輸入的帳號密碼通過驗證，則會重導到 Webapps 並以已登入（有授權身分）的狀態進入系統。

問題會發生在：通過 OIDC 帳號密碼驗證之後重導回 Webapps 時，又被 Webapps 識別成尚未登入而再次重導回 OIDC，
然而 OIDC 實質上已經得到完成登入的 session 所以會自動地重導回 Webapps 然後開始循環這個過程，
直到一定的次數後，Chrome 判定為 ERR_TOO_MANY_REDIRECTS 而停止。

如果要更深入研究問題的根本原因可能會與 MITRE 提供的 library 裡面的 `OIDCAuthenticationFilter` 有關，
因為當從 OIDC 完成驗證後重導回 Webapps 時會附帶參數，`OIDCAuthenticationFilter` 透過參數來決定對使用者授權與否，
是甚麼原因讓完成 OIDC 驗證帶回的參數被 `OIDCAuthenticationFilter` 識別為無效（導致使用者未完成登入的事實）先不在此做討論，
因為從結論來看這**可能**不是原罪。


解答篇
======

在尋找 workaround 的過程中意外發現了這個現象的肇因，
假設 Webapps 的 host 是 foo.bar.com 而 ContextPath 是 WTF，
一般來說 URL 會是 `http://foo.bar.com/WTF/`，
會造成問題描述提及的現象則是輸入了 `http://foo.bar.com/WTF` 這樣子的 URL，
也就是說，trailing 是否加上 slash 直接導致了這個現象的發生與否。

循線追查，部分瀏覽器（如 Firefox）會在**特定情況下**自動地在 trailing 補上 slash，
所以不會發生此問題。

在「trailing 補上 slash」此問題的追查上，最後發現是 tomcat 的版本在作怪，
參考 Changelog 的 `Tomcat 8.0.29` 中提到：

> Move the functionality that provides redirects for context roots and directories where a trailing / is added from the Mapper to the DefaultServlet. This enables such requests to be processed by any configured Valves and Filters before the redirect is made. This behaviour is configurable via the mapperContextRootRedirectEnabled and mapperDirectoryRedirectEnabled attributes of the Context which may be used to restore the previous behaviour. (markt)


簡單的說就是在 `8.0.29` 新增了一個設定，可以讓「trailing 補上 slash」這件事情可以 config，
但是，預設值是 `false`（文件並未提到），直到 `8.0.31` 才將此設定值的預設改為 `true`，
但 `8.0.31` 沒有 release 所以實質上到了 `8.0.32` 才改正了這個現象。
而在 `8.0.29` 與 `8.0.30` 要避免這個問題只要在 `context.xml` 內設定：

```xml
<Context mapperContextRootRedirectEnabled="true">
...
</Context>
```


也同樣可以得到一樣的效果 [蓋章]。


註：

1. 問題描述省略了 OIDC 詢問 user 是否授權該網站的步驟
1. OIDC 流程可以參考 [OpenID Connect flow]


[MITRE]: https://github.com/mitreid-connect/
[OpenID Connect flow]: https://docs.axway.com/u/documentation/api_gateway/7.5.1/webhelp_portal_oauth/Content/OAuthGuideTopics/OpenidImport/openid_flow.htm

版本：`Git-2.9.3-64-bit`

因為紀錄帳號密碼需要[這個](https://github.com/Microsoft/Git-Credential-Manager-for-Windows)功能

所以安裝前先確認 `.NET Framework` 是否有安裝（4.5 以上）


安裝後設定：

```
git config --global user.name "ybon3"
git config --global user.email "lee.ybon@gmail.com"

git config --global core.autocrlf true
git config --global core.whitespace cr-at-eol
```

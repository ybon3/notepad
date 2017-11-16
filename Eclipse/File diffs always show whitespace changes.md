檔案 diff 時（Git）總是整個檔案都顯示不同
--------------------------------------

Reference: https://www.eclipse.org/forums/index.php/t/367532/


首先確認 Git 的設定，在 git bash 輸入：

```
git config --list
```

確認

```
core.autocrlf=true
core.whitespace=cr-at-eol
```

這兩項是否存在，或者直接執行 global 設定：

```git config --global core.autocrlf true```

```git config --global core.whitespace cr-at-eol```


完成上述設定後，若 Eclipse 檔案 diff 的問題仍未改善，應該就是 
https://www.eclipse.org/forums/index.php?t=msg&th=367532&goto=1727544&#msg_1727544 提到的問題（他也說了解法）

進入 `Window -> Preference -> Team -> Git -> Configuration` 確認否有 `core.autocrlf` 及 `core.whitespace` 的設定值，
如果沒有就手動加上去吧，這是 EGIT 沒有正確抓取 git global setting 的問題。

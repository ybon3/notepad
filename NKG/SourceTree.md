學習筆記
=======

## 如何進行 Rebase (Ver. 1)

1. 切換到要被 rebase 的 branch
2. Pull -> 選擇 Remote branch to pull -> checked "Rebase instead of merge" -> OK
  
   或者右鍵點選要用來 Rebase 的 branch -> Rebase current changes onto ... -> OK
   
3. 若有 Conflict 重複此階段直到全部修正：

    1. Fix conflict
    2. Stage fixed files
    3. Action -> Continue Rebase
    4. 出現新的 Conflict，回到 1.

4. Pull -> remote 的自己這個 branch
5. 如果有 Conflict 就修正 -> Stage -> Commit

*--------- Now can push to remote ---------*

6. 可以與原本 rebase 的 branch 進行 merge


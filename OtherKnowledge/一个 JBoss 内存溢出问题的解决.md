Reference: https://my.oschina.net/daxiaoming/blog/599799


一、背景介绍
-----------

公司对外提供了一个API系统，提供了产品的搜索，查询，价格以及订单相关等功能，由我来负责开发维护。这个系统部署在一个独立的 JBoss 实例里。大概从2015年4月以来，该系统不定时 down 机。因为同一个服务器上还跑了另外两个Jboss，一开始还以为是服务器物理内存不够，API系统挂了之后，只是简单地重启了一下相关的Jboss。最近将其中的一个Jboss迁移到别的服务器了，但是API系统还是会不定时地挂掉，于是特地安排了时间来跟踪一下这个问题。


二、前期准备
-----------

首先打开GC日志，在Jboss的 bin目录里面的 standalone.conf 文件里面的 JAVA_OPTS 行里面，加上这两个选项：  “-verbose.gc  -Xloggc:/server/gc.log”；

接着，打开OOM 时记录 HeapDump的选项。在 standalone.conf 文件里面的 JAVA_OPTS 行里面，加上这个选项  “-XX:+HeapDumpOnOutOfMemoryError”；

最后，重新启动 jboss。等待下一次API系统 down 机。


幸运地（也可以说不幸），重启之后第三天，API系统又挂了。从服务器取回GC日志（/server/gc.log），还有内存溢出HeapDump文件（java_pid14584.hprof， 在 bin 目录下，文件名可能不是这个，但是类似于这个）。


三、分析
-------

1. 分析GC活动
使用 gcviewer 打开下载回来的 gc.log，发现GC方面没有什么问题，内存占用挺正常的。到最后down机前，都没有发现GC异常。

2. 分析 HeapDump 文件
使用 Eclipse 的 MAT 工具打开下载回来的  java_pid14584.hprof 文件，然后等待MAT分析完成。


打开 Leak Suspects，看看有哪些可疑的线程：

嗯？http-executor-threads 这个线程占用了 800多M的内存？点 “See stacktrace” 去看看它的调用栈。

CommonDateUtil的getBetweenDates() 方法导致了异常？先看看这个方法是干啥的


```java
public static List<String> getBetweenDates(String day1, String day2) {
     List<String> dates = new ArrayList<String>();
     if(Assert.isNullOrEmpty(day2) || day1.equals(day2)){
        dates.add(day1);
        return dates;
     }
     Calendar startCalendar = Calendar.getInstance();
     Calendar endCalendar = Calendar.getInstance();
     SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd"); 
     Date startDate = null;
     Date endDate = null;
     try {
         endDate = df.parse(day2);
         startDate = df.parse(day1);
         if(endDate.before(startDate)){
             throw new RuntimeException("日期有错");
         }
     } catch (ParseException e) {
         e.printStackTrace();
         return dates;
     } 
     endCalendar.setTime(endDate);
     dates.add(df.format(startDate)); 
     while (true) {
         startCalendar.add(Calendar.DAY_OF_MONTH, 1);
         if (startCalendar.getTimeInMillis() < endCalendar.getTimeInMillis()) {
             dates.add(df.format(startCalendar.getTime()));
         } else {
             break;
         }
     }
     return dates;
 }
```

大略看了一下，参数合理的话，应该没有什么问题啊。

再去看看内存占用，这个占用了 800多M内存的线程，到底都存了些什么？回到 Overview，点击 “Histogram”，看看各类占用的内存

咦？怎么有个 ArrayList 占用了 800多M 的内存？右击这一行，选择  “list objects”   →  “with outgoing references”，看看这个 ArrayList 里面到底存了些什么？

到了这个页面，点击展开那个 800多M的 ArrayList的 elementData。看看具体元素的内容。因为该 ArrayList 的内容较多，展开的时间会比较长（我的I7-4800MQ /12G内存，也花了2分钟）。

怎么都是日期类的字符串？这不是那个CommonDateUtil的getBetweenDates() 产生的日期字符串吗？怎么加到了 2069-11-19? 死循环了吗？



四、解决办法
-----------

1. 简单解决
既然问题出在CommonDateUtil的getBetweenDates() 方法里，再加上一层限制，防止死循环就行了。例如：只返回最多一年的日期字符串。
2. 根本解决
在API接口层次作限制，每次只能小批量返回一部分的价格，例如说 31 天。



五、文中用到的工具
----------------

1. GcViewer
 图形化查看 JVM GC活动的工具。1.33及之前版本可以使用JDK1.7运行，1.34开始需要使用JDK1.8运行。下载地址： http://sourceforge.net/projects/gcviewer/

2. Eclipse Memory Analyzer (MAT)
多维度分析 HeapDump 文件的工具。由于 HeapDump 文件比较大，建议下载64位的版本来使用。下载地址：  http://www.eclipse.org/mat/downloads.php

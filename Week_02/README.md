# 学习笔记



### 串行GC

1. Young：单线程，使用标记-复制算法
2. Old：单线程，使用标记-清除-整理算法
3. 命令：-XX:+UseSerailGC
4. 场景：CPU利用率高，需要STW的时间长，适用于单核CPU，现在基本不用；
5. -XX:+UseParNewGC改进版的Serial GC，可以配合CMS使用，对Young区进行GC

### 并行GC

1. Young：-XX:+UseParallelGC，标记-复制算法
2. Old：-XX:+UseParallelOldGC，标记-清除-整理算法
3. 命令：-XX:ParallelGCThreads=N来指定GC线程数，默认是CPU核心数
4. 场景：多核服务器，暂停时间比串行GC时间更短，两次GC间隔期，没有GC线程在运行，只会在GC的时候才会创建GC线程，不消耗任何系统资源
5. 目标：增加吞吐量
6. JDK8默认的GC

### CMS GC

1. Young：一般配合使用ParNewGC，标记-复制算法

2. Old：标记-清除算法

3. 命令：-XX:+UseConcMarkSweepGC

4. 场景：暂停时间短

5. 目标：暂停时间短，通过分解各个阶段来实现，对老年代不进行整理，使用空闲列表来管理内存空间，标记-清除的大部分工作和应用线程一起并发执行；

   和应用线程争抢CPU时间，CMS使用的并发线程数等于CPU核心数的1/4。

6. 6个阶段：

   1. 初始标记  STW，只标记被GC ROOTS对象引用的第一个对象
   2. 并发标记
   3. 并发预处理 卡片标记
   4. 最终标记 STW
   5. 并发清除
   6. 并发重置

7.  问题：老年代内存碎片问题，因为没有压缩，在某些情况下GC会造成不可预测的暂停时间，特别是堆内存较大的情况

8. JDK9以后CMS不是主流，慢慢会废弃

### G1 GC

1. 2048个区域region

2. 命令：-XX:+UseG1GC   -XX:MaxGCPauseMillis=50   -XX:ConcGCThreads (GC线程数量，默认是Java线程的1/4)

   -XX:+InitiatingHeapOccupancyPercent（G1内部并行回收循环启动的阈值，默认是堆内存的45%，当老年代使用大于等于45%的时候，JVM启动垃圾回收）

3. 目标：将STW停顿的时间和分布，变成可预期且可配置的

4. 步骤：

   1. 年轻代模式转移暂停
   2. 并发标记（类似于CMS）
   3. 转移暂停：混合模式

5. 注意事项：以下3种情况G1出发Full GC，G1会退化成Serial收集器来完成垃圾回收的工作

   1. 并发模式失败
   2. 晋升失败
   3. 巨型对象分配失败

6. JDK11默认的GC是G1
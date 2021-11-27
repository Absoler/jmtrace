# jmtrace

南京大学课程《软件工程研究导引》作业

功能：对给定jar包，记录其运行时所有memory access的信息

# 使用说明

## Dependency

`asm-9.2.jar`

放在lib目录下，可从https://repository.ow2.org/nexus/content/repositories/releases/org/ow2/asm/asm/9.2/下载

## Build

进入项目目录下，执行

```
$ make 
```

即可在当前目录生成out目录和jmtrace.jar、

同时实现了 `make clean` 和 `make install` 

## Run

jmtrace是一个脚本，内容为

```bash
#!/bin/bash
java -javaagent:jmtrace.jar -jar $1
```

使用方法：

```
jmtrace test.jar
```

样例输出：

```
W 1 728938a9 Test.id
W 1 6438a396 int[0]
R 1 6438a396 int[0]
W 1 6438a396 int[1]
R 1 6438a396 int[1]
W 1 6438a396 int[2]
R 1 6438a396 int[2]
W 1 6438a396 int[3]
R 1 6438a396 int[3]
W 1 6438a396 int[4]
W 1 728938a9 Test.id
W 1 e2144e4 Test.o
```
# spring-boot-seed

A java restful web seed project based spring boot framework.

[TOC]

## Getting Start

```
git clone https://github.com/qianhaikeji/spring-boot-seed.git
```

### IDE

1. 使用eclipse，安装eclipse gradle插件。
2. 以gradle的方式导入工程，**导入过程中注意配置gradle路径**。
3. 由于项目使用了@Getter等注解，使用eclipse需要安装Lombok插件.

```
1.安装项目包依赖
2.找到依赖lombok的路径，执行 java -jar lombok-1.16.6.jar
3.选择正确的eclipse应用目录
4.重启eclipse
5.在IDE 中 refresh gradle
```

### Database

#### Mongo

使用mongo3.2，安装完成后，需要配置用户，方法如下。

1.创建admin数据库，用于管理mongo用户。为admin数据库添加一个管理员，该管理员拥有admin数据库的权限

```
use admin
db.createUser({user:"dba",pwd:"dba",roles: [ { role: "userAdminAnyDatabase", db: "admin" } ]})
```

2.创建一个数据库cxszsh，并为该数据库添加一个拥有读写权限的用户

```
use sbseed   #创建数据库
db.createUser({user: "admin", pwd: "sbseed", roles: [{ role: "readWrite", db: "sbseed" }]})
```

注意：用户必须在当前数据库下授权，即“数据库帐号是跟着数据库来走的，哪里创建哪里认证。”
参考网址：http://www.cnblogs.com/zhoujinyi/p/4610050.html

#### Mysql



#### Redis



## Deployment

生成war包

```
gradle build
```
跳过单元测试
```
gradle build -x test
```
## Directory Introduction

```
.
├── README.md
├── bin
├── build        # 编译声称文件
├── build.gradle # gradle依赖配置
├── docs         # 文档
├── gradle
├── libs         # 本地依赖库
├── scripts      # 工程使用的自动化脚本
└── src          # 源码
```

##Reference
1. [mongo](https://docs.mongodb.com/v3.0): 数据库文档
2. [spring mongo](http://docs.spring.io/spring-data/data-mongo/docs/1.8.4.RELEASE/reference/html/): spring集成mongo
3. [jersey](https://waylau.gitbooks.io/jersey-2-user-guide/content/): spring restful支持库
4. [spring security](https://vincentmi.gitbooks.io/spring-security-reference-zh/content/): 认证鉴权



[其它项目资源](https://github.com/qianhaikeji/develop-guide.git)
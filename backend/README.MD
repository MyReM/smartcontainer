## 项目结构

##### 项目框架
springBoot、mybatis、jpa、fastjson、redis、shiro、druid

* labelPrint
    * common
    * core
    * print
    * web
    
注意：

1、sql和backup只是文件夹并不是模块，目前sql文件夹设计用来存放数据库结构和数据的sql文件

2、提交更新时不要把*.iml文件和.idea文件夹提交上来，否则下次checkout初始化项目时会出现意想不到的问题

3、代码提交前先update、代码冲突时，先进行业务交流，分析代码实现的业务，再解决冲突，勿贪快

4、提交记录一定要写，便于后来开发者读懂你提交的意图

5、遵循 "约定优于配置" 
# 监听kafka消费
+ 任务：主要监听车辆上传的信息值，通过kafka的topic传输,实时添加到MySQL中
+ 可能遇到的问题：
    + 数据以后量太大、是否考虑迁移到其他数据库。例如：MongoDB、HBASE等
## 项目结构
+ 该项目集成了kafka的生产和消费端，分别在主包/config文件下配置
+ 同时集成了webApi功能，模拟在web端动态生成源数据,访问路径：http://localhost:9998/kafka/send?message=这是kafka的具体内容
+ /consumer文件夹下是具体的消费执行业务逻辑
+ /controller、/dao、/entity、/service等几个文件夹是对业务逻辑的添加到MySQL代码执行
+ /produce 文件为模拟kafka生成端的生产数据和推送kafka流程记录。以后可以作为生成kafka的基础代码做二次开发
+ /resources 为资源文件夹
    + /mapper文件夹下位对应的xml配置信息，路径寻找在yml中配置可见
    + *.yml结尾的文件为对应的项目配置文件，具体情况看注释
## 启动顺序
1. 启动服务：
    1. 先启动zookeeper
        ```java 
            bin/zkServer.sh start 
       ```
    2. 再启动kafka
        ```java
           // windows下是这样启动的    
           .\bin\windows\kafka-server-start.bat .\config\server.properties
        ```
2. 修改application-dev.yml文件中的kafka端口为上述顺序的端口号。然后启动主程序，并设置application.yml中的spring.profiles.active=dev 
3. 在浏览器中输入：`http://localhost:9998/kafka/send?message=测试报文` 即可
# http client 测试

## 打包

```shell
# 在项目根目录执行如下命令
./mvnw clean compile assembly:single -pl http-client -am
```

## 运行

```shell
# 在项目根目录执行如下命令
java -jar http-client/target/http-client-1.0-SNAPSHOT-jar-with-dependencies.jar
```
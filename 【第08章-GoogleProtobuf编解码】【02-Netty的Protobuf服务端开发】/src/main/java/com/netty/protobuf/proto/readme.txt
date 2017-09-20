将src目录拷贝到protoc.exe所在的目录，将源码目录拷贝到src目录，使用下面的命令进行编译

protoc.exe --proto_path=src  --java_out=src src/com/netty/protobuf/proto/SubscripeReq.proto
protoc.exe --proto_path=src  --java_out=src src/com/netty/protobuf/proto/SubscripeResp.proto
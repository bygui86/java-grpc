
# java-grpc
gRPC example in Java

## Services

- [greeting service (gRPC client)](greeting-service)
- [hello service (gRPC server)](hello-service)

---

## Run

1. Start hello service (server)
	```shell
	cd hello-service
	mvnw clean spring-boot:run
	```

2. In another shell, start greeting service (client)
	```shell
	cd greeting-service
	mvnw clean spring-boot:run
	```

---

## Polyglot test

This repo can be used for a polyglot test together with [go-grpc](https://github.com/bygui86/go-grpc)

### Go client --> Java server

1. Start server (hello-service)
	```shell
	cd java-grpc/hello-service
	mvnw clean spring-boot:run
	```

2. In another shell, start client (greeting-service)
	```shell
	cd go-grpc/greeting-service
	GO111MODULE=on go run main.go
	```

### Java client --> Go server

1. Start server (hello-service)
	```shell
	cd go-grpc/hello-service
	GO111MODULE=on go run main.go
	```

2. In another shell, start client (greeting-service)
	```shell
	cd java-grpc/greeting-service
	mvnw clean spring-boot:run
	```

---

## Links

- https://www.youtube.com/watch?v=BOW7jd136Ok
- https://github.com/LogNet/grpc-spring-boot-starter
- https://github.com/saturnism/grpc-java-by-example
- https://github.com/grpc/grpc-java
- https://www.xolstice.org/protobuf-maven-plugin/usage.html
- https://grpc.io/docs/quickstart/java.html
- https://www.baeldung.com/grpc-introduction
- https://codenotfound.com/grpc-java-example.html
- https://grpc.io/docs/tutorials/basic/java.html
- https://spring.io/blog/2015/03/22/using-google-protocol-buffers-with-spring-mvc-based-rest-services

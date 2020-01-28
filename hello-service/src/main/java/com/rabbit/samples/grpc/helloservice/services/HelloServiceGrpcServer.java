package com.rabbit.samples.grpc.helloservice.services;


import com.rabbit.samples.grpc.helloservice.interceptors.NotSpringLogInterceptor;
import com.rabbit.samples.grpc.helloservice.interceptors.SpringLogInterceptor;
import com.rabbit.samples.grpc.helloservice.proto.HelloRequest;
import com.rabbit.samples.grpc.helloservice.proto.HelloResponse;
import com.rabbit.samples.grpc.helloservice.proto.HelloServiceGrpc;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import org.lognet.springboot.grpc.GRpcService;


/**
 * @author Matteo Baiguini
 * matteo@solidarchitectures.com
 * 05 Apr 2019
 */
@Slf4j
@GRpcService(interceptors = {
		SpringLogInterceptor.class, NotSpringLogInterceptor.class
})
public class HelloServiceGrpcServer extends HelloServiceGrpc.HelloServiceImplBase {

	@Override
	public void sayHello(HelloRequest request, StreamObserver<HelloResponse> responseObserver) {

		String name = request.getName();
		log.info("Received gRPC message with name: {}", name);

		responseObserver.onNext(
				buildResponse(name)
		);
		responseObserver.onCompleted();
	}

	private HelloResponse buildResponse(final String name) {

		return HelloResponse
				.newBuilder()
				.setGreeting("Hi " + name)
				.build();
	}

}

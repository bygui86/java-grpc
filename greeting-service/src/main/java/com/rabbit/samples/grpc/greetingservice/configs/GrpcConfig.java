package com.rabbit.samples.grpc.greetingservice.configs;

import com.rabbit.samples.grpc.helloservice.proto.HelloServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * @author Matteo Baiguini
 * matteo@solidarchitectures.com
 * 10 Apr 2019
 */
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter(AccessLevel.PUBLIC)
@Configuration
public class GrpcConfig {

	@Value("${grpc.host}")
	String host;

	@Value("${grpc.port}")
	int port;

	@Bean
	public ManagedChannel managedChannel() {

		log.debug("Create gRPC ManagedChannel");

		return ManagedChannelBuilder.forAddress(getHost(), getPort()).usePlaintext().build();
	}

	@Bean
	public HelloServiceGrpc.HelloServiceBlockingStub helloServiceBlockingStub(final ManagedChannel managedChannel) {

		log.debug("Create gRPC HelloServiceBlockingStub");

		return HelloServiceGrpc.newBlockingStub(managedChannel);
	}

}

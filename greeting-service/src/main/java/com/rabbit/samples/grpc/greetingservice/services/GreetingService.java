package com.rabbit.samples.grpc.greetingservice.services;

import com.rabbit.samples.grpc.helloservice.proto.HelloRequest;
import com.rabbit.samples.grpc.helloservice.proto.HelloResponse;
import com.rabbit.samples.grpc.helloservice.proto.HelloServiceGrpc;
import io.grpc.StatusRuntimeException;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


/**
 * @author Matteo Baiguini
 * matteo@solidarchitectures.com
 * 05 Apr 2019
 */
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@Getter(AccessLevel.PROTECTED)
@Service
public class GreetingService {

	static final String ERR_MSG_PREFIX = "Error calling gRPC server: ";

	HelloServiceGrpc.HelloServiceBlockingStub helloServiceBlockingStub;

	public String getGreet(final String name) {

		HelloResponse helloResponse;
		try {
			helloResponse = getHelloServiceBlockingStub().sayHello(
					buildRequest(name)
			);
		} catch (StatusRuntimeException e) {
			log.error(ERR_MSG_PREFIX + "{}", e.getMessage());
			return ERR_MSG_PREFIX + e.getMessage();
		}

		if (helloResponse != null) {
			return helloResponse.getGreeting();
		}

		String emptyResponseMsg = ERR_MSG_PREFIX + "empty response";
		log.error(emptyResponseMsg);
		return emptyResponseMsg;
	}

	private HelloRequest buildRequest(final String name) {

		return HelloRequest
				.newBuilder()
				.setName(name)
				.build();
	}

}

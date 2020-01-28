package com.rabbit.samples.grpc.greetingservice;

import io.grpc.ManagedChannel;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PreDestroy;
import java.util.concurrent.TimeUnit;


@AllArgsConstructor
@Getter(AccessLevel.PROTECTED)
@SpringBootApplication
public class GreetingServiceApplication {

	ManagedChannel managedChannel;

	@PreDestroy
	private void destroy() throws InterruptedException {

		getManagedChannel().shutdown().awaitTermination(5, TimeUnit.SECONDS);
	}

	public static void main(String[] args) {
		SpringApplication.run(GreetingServiceApplication.class, args);
	}

}

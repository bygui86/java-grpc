package com.rabbit.samples.grpc.helloservice;

import com.rabbit.samples.grpc.helloservice.proto.HelloRequest;
import com.rabbit.samples.grpc.helloservice.proto.HelloResponse;
import com.rabbit.samples.grpc.helloservice.proto.HelloServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.inprocess.InProcessChannelBuilder;
import io.grpc.stub.StreamObserver;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.lognet.springboot.grpc.autoconfigure.GRpcServerProperties;
import org.lognet.springboot.grpc.context.LocalRunningGrpcPort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.AssertionErrors;
import org.springframework.util.StringUtils;

import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atMost;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.NONE;


/**
 * @author Matteo Baiguini
 * matteo@solidarchitectures.com
 * 10 Apr 2019
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {HelloServiceApplication.class}, webEnvironment = NONE)
public class HelloServiceIntegrationTest {

	ManagedChannel channel;
	ManagedChannel inProcChannel;

	@LocalRunningGrpcPort
	int runningPort;

	@Autowired
	GRpcServerProperties gRpcServerProperties;

	@Before
	public final void setupChannels() {
		if (gRpcServerProperties.isEnabled()) {
			channel = ManagedChannelBuilder
					.forAddress("localhost", runningPort)
					.usePlaintext()
					.build();
		}

		if (StringUtils.hasText(gRpcServerProperties.getInProcessServerName())) {
			inProcChannel = InProcessChannelBuilder
					.forName(gRpcServerProperties.getInProcessServerName())
					.usePlaintext()
					.build();
		}
	}

	@After
	public void tearDown() {
		Optional.ofNullable(channel).ifPresent(ManagedChannel::shutdownNow);
		Optional.ofNullable(inProcChannel).ifPresent(ManagedChannel::shutdownNow);
	}

	@Test
	final public void testBlockingStub() {

		// given
		String name ="Matt";
		final HelloServiceGrpc.HelloServiceBlockingStub blockingStub = HelloServiceGrpc.newBlockingStub(Optional.ofNullable(channel).orElse(inProcChannel));

		// when
		final HelloRequest helloRequest = HelloRequest.newBuilder().setName(name).build();
		final String greeting = blockingStub.sayHello(helloRequest).getGreeting();

		// then
		Assert.assertNotNull("Replay should not be null",greeting);
		AssertionErrors.assertTrue(String.format("Replay should contain name '%s'",name),greeting.contains(name));
	}

	@Test
	final public void testAsyncStub() throws InterruptedException {

		// given
		String name ="Matt";
		final HelloServiceGrpc.HelloServiceStub asyncStub = HelloServiceGrpc.newStub(Optional.ofNullable(channel).orElse(inProcChannel));
		StreamObserver<HelloResponse> responseObserver = (StreamObserver<HelloResponse>) mock(StreamObserver.class);

		// when
		final HelloRequest helloRequest = HelloRequest.newBuilder().setName(name).build();
		asyncStub.sayHello(helloRequest, responseObserver);

		// then
		verify(responseObserver, atMost(1)).onNext(any(HelloResponse.class));
		verify(responseObserver, never()).onError(any(Throwable.class));
	}

	@Test
	final public void testFutureStub() throws ExecutionException, InterruptedException {

		// given
		String name ="Matt";
		final HelloServiceGrpc.HelloServiceFutureStub futureStub = HelloServiceGrpc.newFutureStub(Optional.ofNullable(channel).orElse(inProcChannel));

		// when
		final HelloRequest helloRequest = HelloRequest.newBuilder().setName(name).build();
		final String reply = futureStub.sayHello(helloRequest).get().getGreeting();

		// then
		Assert.assertNotNull("Replay should not be null",reply);
		AssertionErrors.assertTrue(String.format("Replay should contain name '%s'",name),reply.contains(name));
	}

}

package com.rabbit.samples.grpc.greetingservice;

import com.rabbit.samples.grpc.greetingservice.services.GreetingService;
import com.rabbit.samples.grpc.helloservice.proto.HelloRequest;
import com.rabbit.samples.grpc.helloservice.proto.HelloResponse;
import com.rabbit.samples.grpc.helloservice.proto.HelloServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.inprocess.InProcessChannelBuilder;
import io.grpc.inprocess.InProcessServerBuilder;
import io.grpc.stub.StreamObserver;
import io.grpc.testing.GrpcCleanupRule;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;

import static org.junit.Assert.assertEquals;
import static org.mockito.AdditionalAnswers.delegatesTo;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;


/**
 * @author Matteo Baiguini
 * matteo@solidarchitectures.com
 * 10 Apr 2019
 */
@RunWith(BlockJUnit4ClassRunner.class)
public class GreetingServiceUnitTest {

	/**
	 * This rule manages automatic graceful shutdown for the registered servers and channels at the end of test.
	 */
	@Rule
	public GrpcCleanupRule grpcCleanupRule = new GrpcCleanupRule();

	HelloServiceGrpc.HelloServiceImplBase grpcFakeServer = mock(
			HelloServiceGrpc.HelloServiceImplBase.class,
			delegatesTo(new HelloServiceGrpc.HelloServiceImplBase() {
				@Override
				public void sayHello(HelloRequest request, StreamObserver<HelloResponse> responseObserver) {
					responseObserver.onNext(
							HelloResponse
									.newBuilder()
									.setGreeting(request.getName())
									.build()
					);
					responseObserver.onCompleted();
				}
			})
	);

	GreetingService greetingService;

	@Before
	public void setUp() throws Exception {

		// Generate a unique in-process server name.
		String serverName = InProcessServerBuilder.generateName();

		// Create a server, add service, start, and register for automatic graceful shutdown.
		grpcCleanupRule.register(
				InProcessServerBuilder
						.forName(serverName)
						.addService(grpcFakeServer)
						.directExecutor()
						.build()
						.start()
		);

		// Create a client channel and register for automatic graceful shutdown.
		ManagedChannel managedChannel = grpcCleanupRule.register(
				InProcessChannelBuilder
						.forName(serverName)
						.directExecutor()
						.build()
		);

		// Create a blocking stub
		HelloServiceGrpc.HelloServiceBlockingStub blockingStub = HelloServiceGrpc.newBlockingStub(managedChannel);

		// Create a client using the in-process channel;
		greetingService = new GreetingService(blockingStub);
	}

	/**
	 * To test the client, call from the client against the fake grpcFakeServer, and verify behaviors or state changes from the grpcFakeServer side.
	 */
	@Test
	public void testHelloServiceGrpc() {

		// given
		String name = "Matt";
		ArgumentCaptor<HelloRequest> requestCaptor = ArgumentCaptor.forClass(HelloRequest.class);

		// when
		greetingService.getGreet(name);

		// verify
		verify(grpcFakeServer).sayHello(requestCaptor.capture(), ArgumentMatchers.any());

		// then
		assertEquals(name, requestCaptor.getValue().getName());
	}

}

package com.rabbit.samples.grpc.helloservice;

import com.rabbit.samples.grpc.helloservice.proto.HelloRequest;
import com.rabbit.samples.grpc.helloservice.proto.HelloResponse;
import com.rabbit.samples.grpc.helloservice.proto.HelloServiceGrpc;
import com.rabbit.samples.grpc.helloservice.services.HelloServiceGrpcServer;
import io.grpc.ManagedChannel;
import io.grpc.inprocess.InProcessChannelBuilder;
import io.grpc.inprocess.InProcessServerBuilder;
import io.grpc.testing.GrpcCleanupRule;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import java.io.IOException;

import static org.junit.Assert.assertTrue;


/**
 * @author Matteo Baiguini
 * matteo@solidarchitectures.com
 * 11 Apr 2019
 */
@RunWith(BlockJUnit4ClassRunner.class)
public class HelloServiceUnitTest {

	/**
	 * This rule manages automatic graceful shutdown for the registered servers and channels at the end of test.
	 */
	@Rule
	public final GrpcCleanupRule grpcCleanupRule = new GrpcCleanupRule();

	HelloServiceGrpc.HelloServiceBlockingStub blockingStub;

	@Before
	public void setUp() throws IOException {

		// Generate a unique in-process server name.
		String serverName = InProcessServerBuilder.generateName();

		// Create a server, add service, start, and register for automatic graceful shutdown.
		grpcCleanupRule.register(
				InProcessServerBuilder
						.forName(serverName)
						.addService(new HelloServiceGrpcServer())
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

		// Create a client stub
		blockingStub = HelloServiceGrpc.newBlockingStub(managedChannel);
	}

	/**
	 * To test the server, make calls with a real stub using the in-process channel, and verify behaviors or state changes from the client side.
	 */
	@Test
	public void testGrpcServer() {

		// given
		String name = "Matt";

		// when
		HelloResponse response = blockingStub.sayHello(
				HelloRequest
						.newBuilder()
						.setName(name)
						.build()
		);

		// then
		assertTrue(response.getGreeting().contains(name));
	}

}

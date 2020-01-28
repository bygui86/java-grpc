package com.rabbit.samples.grpc.greetingservice.controllers;

import com.rabbit.samples.grpc.greetingservice.services.GreetingService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * @author Matteo Baiguini
 * matteo@solidarchitectures.com
 * 05 Apr 2019
 */
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@Getter(AccessLevel.PROTECTED)
@RestController
@RequestMapping("/greeting")
public class GreetingController {

	GreetingService greetingService;

	@GetMapping("/{name}")
	public String greet(@PathVariable final String name) {

		log.info("Greeting {}", name);

		return getGreetingService().getGreet(name);
	}

}

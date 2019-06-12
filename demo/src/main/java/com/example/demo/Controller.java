package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class Controller {
	
	@Autowired
	private RestTemplate restTemplate;

	@GetMapping("/muthu")
	public ResponseEntity<String> hello() {
		return restTemplate.getForEntity("https://jsonplaceholder.typicode.com/todos/1", String.class);
	}

}

package com.example.initializer;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.function.Function;

@SpringBootApplication
public class Initializer {

	private final String baseUri;
	private final RabbitTemplate rabbitTemplate;
	private final String topicExchange;
	private final String routingKey;

	public static void main(String[] args) {
		SpringApplication.run(Initializer.class, args);
	}

	public Initializer(@Value("${initializer.third-party-uri}") String baseUri,
					   @Value("${initializer.topicExchange}") String topicExchange,
					   @Value("${initializer.routingKey}") String routingKey,
					   RabbitTemplate rabbitTemplate){
		this.baseUri = baseUri;
		this.rabbitTemplate = rabbitTemplate;
		this.topicExchange = topicExchange;
		this.routingKey = routingKey;
	}

	@Bean
	public Function<LatLng, String> sunriseSunset(){
		return input -> {
			Mono<String> responseFromApi = webClient()
					.get()
					.uri(String.format("?lat=%s&lng=%s",input.lat(),input.lng()))
					.retrieve()
					.bodyToMono(String.class);

			responseFromApi.subscribe(result -> rabbitTemplate
					.convertAndSend(topicExchange,routingKey,result));
			System.out.println("****** Third Party Message - sent to processor");
			return "Success?";
		};
	}

	@Bean
	public WebClient webClient(){
		return WebClient.create(baseUri);
	}
}

record LatLng (String lat, String lng){}

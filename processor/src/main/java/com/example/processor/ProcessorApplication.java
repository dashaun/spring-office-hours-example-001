package com.example.processor;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@SpringBootApplication
public class ProcessorApplication implements MessageListener {

	private final String baseUri;
	private final String queueName;
	private final String topicExchange;
	private final String routingKey;

	public ProcessorApplication(@Value("${sinkUri}")String baseUri,
								@Value("${queueName}") String queueName,
								@Value("${topicExchange}")String topicExchange,
								@Value("${routingKey}")String routingKey){
		this.baseUri = baseUri;
		this.queueName = queueName;
		this.topicExchange = topicExchange;
		this.routingKey = routingKey;
	}

	public static void main(String[] args) {
		SpringApplication.run(ProcessorApplication.class, args);
	}

	@Bean
	TopicExchange exchange(){
		return new TopicExchange(topicExchange);
	}

	@Bean
	Queue queue() {
		return new Queue(queueName, false);
	}

	@Bean
	Binding binding(Queue queue, TopicExchange exchange){
		return BindingBuilder
				.bind(queue)
				.to(exchange)
				.with(routingKey);
	}

	@Bean
	public WebClient webClient(){
		return WebClient.create(baseUri);
	}

	@Bean
	public SimpleMessageListenerContainer container(ConnectionFactory connectionFactory){
		SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
		container.setConnectionFactory(connectionFactory);
		//Do I need this?
		container.setQueueNames(queueName);
		container.setMessageListener(this);
		return container;
	}

	@Override
	public void onMessage(Message message) {
		Mono<String> response = webClient()
				.post()
				.bodyValue(new String(message.getBody()))
				.retrieve().bodyToMono(String.class);

		response.subscribe(System.out::println);
	}
}

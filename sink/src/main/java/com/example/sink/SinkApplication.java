package com.example.sink;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.function.Function;

@SpringBootApplication
public class SinkApplication {

	public static void main(String[] args) {
		SpringApplication.run(SinkApplication.class, args);
	}

	@Bean
	public Function<SinkMessage,String> consume(){
		return value -> {
			System.out.println(value);
			return "*** Message delivered to Sink";
		};
	}
}

record SinkMessage(Results results, String status) {}

record Results(String sunrise,
			   String sunset,
			   String solar_noon,
			   String day_length,
			   String civil_twilight_begin,
			   String civil_twilight_end,
			   String nautical_twilight_begin,
			   String nautical_twilight_end,
			   String astronomical_twilight_begin,
			   String astronomical_twilight_end) {}

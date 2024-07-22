package com.example.demo

import com.launchdarkly.sdk.server.LDClient
import org.modelmapper.ModelMapper
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.context.annotation.Bean
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.web.client.RestTemplate


@SpringBootApplication
@EnableScheduling
class DemoApplication {
	@Bean
	fun modelMapper(): ModelMapper {
		return ModelMapper()
	}

	@Bean
	fun restTemplate(builder: RestTemplateBuilder): RestTemplate {
		return builder.build()
	}

	@Bean
	fun ldClient(): LDClient {
		return LDClient("sdk-b3013cfa-88f1-4b22-8aff-0bd932854c91")
	}
}

fun main(args: Array<String>) {
	runApplication<DemoApplication>(*args)
}

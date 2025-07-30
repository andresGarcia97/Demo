package com.demo.advanced;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.autoconfigure.cache.CachesEndpointAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.cache.CacheAutoConfiguration;
import org.springframework.boot.autoconfigure.r2dbc.R2dbcAutoConfiguration;
import org.springframework.boot.autoconfigure.web.reactive.ReactiveWebServerFactoryAutoConfiguration;
import org.springframework.boot.autoconfigure.web.reactive.WebFluxAutoConfiguration;

@SpringBootApplication(exclude = {
		CacheAutoConfiguration.class,
		CachesEndpointAutoConfiguration.class,
		WebFluxAutoConfiguration.class,
		ReactiveWebServerFactoryAutoConfiguration.class,
		R2dbcAutoConfiguration.class
})
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

}

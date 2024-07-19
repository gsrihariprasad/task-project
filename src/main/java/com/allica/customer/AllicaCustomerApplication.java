package com.allica.customer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.validation.annotation.Validated;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.allica.customer.repository")
@Validated
public class AllicaCustomerApplication {

	public static void main(String[] args) {
		SpringApplication.run(AllicaCustomerApplication.class, args);
	}

}

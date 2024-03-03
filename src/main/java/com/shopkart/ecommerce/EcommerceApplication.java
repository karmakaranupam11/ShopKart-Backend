package com.shopkart.ecommerce;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(
		title="Shop Kart Backend Server",
		description="This is Documentation for the backend for Shop Karts Api's you can easily integrate",
		version="v1",
		contact=@Contact(
				name="akarmakar",
				email="akarmakar"
		),
		license=@License(
				name="akarmakar"
		)
))
public class EcommerceApplication {

	public static void main(String[] args) {
		SpringApplication.run(EcommerceApplication.class, args);
	}

}

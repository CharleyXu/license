package com.xu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@PropertySource({"license-config.properties"})
public class LicenseApplication {

	public static void main(String[] args) {
		SpringApplication.run(LicenseApplication.class, args);
	}
}

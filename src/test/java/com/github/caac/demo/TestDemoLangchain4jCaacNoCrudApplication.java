package com.github.caac.demo;

import org.springframework.boot.SpringApplication;

public class TestDemoLangchain4jCaacNoCrudApplication {

	public static void main(String[] args) {
		SpringApplication.from(DemoLangchain4jCaacNoCrudApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}

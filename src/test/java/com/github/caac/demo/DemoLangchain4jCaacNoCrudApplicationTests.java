package com.github.caac.demo;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
class DemoLangchain4jCaacNoCrudApplicationTests {

	@Test
	void contextLoads() {
	}

}

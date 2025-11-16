package com.org.user.credentials;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;

@SpringBootTest
@Profile("!integration")
class CredentialsAppApplicationTests {

	@Test
	void contextLoads() {
	}

}

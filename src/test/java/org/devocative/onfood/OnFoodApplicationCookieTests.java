package org.devocative.onfood;

import org.devocative.artemis.ArtemisExecutor;
import org.devocative.artemis.cfg.Config;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test-cookie")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class OnFoodApplicationCookieTests {

	@LocalServerPort
	private int port;

	@Test
	void contextLoads() {
		ArtemisExecutor.run(new Config("artemis-cookie", "artemis")
			.setBaseUrl(String.format("http://localhost:%s/api", port))
		);
	}

}

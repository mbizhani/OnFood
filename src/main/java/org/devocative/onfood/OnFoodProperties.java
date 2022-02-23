package org.devocative.onfood;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "on-food")
public class OnFoodProperties {
	private Security security = new Security();

	@Getter
	@Setter
	public static class Security {
		private ETokenMedia tokenMedia = ETokenMedia.cookie;
		private String tokenKey = "Authorization";
	}

	public enum ETokenMedia {
		header, cookie
	}
}

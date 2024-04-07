package team.exlab.ecohub.news;

import lombok.Getter;
import lombok.Setter;

@org.springframework.boot.context.properties.ConfigurationProperties(prefix = "resources")
@Getter
@Setter
public class StorageConfigurationProperties {
	private String imagesDirectory;
}

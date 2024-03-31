package team.exlab.ecohub;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import team.exlab.ecohub.news.StorageConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(StorageConfigurationProperties.class)
public class EcohubApplication {
    public static void main(String[] args) {
        SpringApplication.run(EcohubApplication.class, args);
    }
}

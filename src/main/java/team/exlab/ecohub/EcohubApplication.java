package team.exlab.ecohub;

import lombok.Getter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class EcohubApplication {
    private static String superAdminPassword;
    private static String firstAdminPassword;

    public static void main(String[] args) {
        superAdminPassword = args[0];
        firstAdminPassword = args[1];
        SpringApplication.run(EcohubApplication.class, args);
    }

    public static String getSuperAdminPassword() {
        return superAdminPassword;
    }

    public static String getFirstAdminPassword() {
        return firstAdminPassword;
    }
}

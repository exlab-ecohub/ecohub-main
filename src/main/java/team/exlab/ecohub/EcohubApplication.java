package team.exlab.ecohub;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class EcohubApplication {
    private static String superAdminLogin;
    private static String superAdminPassword;
    private static String firstAdminLogin;
    private static String firstAdminPassword;

    public static void main(String[] args) {
        superAdminLogin = args[0];
        superAdminPassword = args[1];
        firstAdminLogin = args[2];
        firstAdminPassword = args[3];
        SpringApplication.run(EcohubApplication.class, args);
    }

    public static String getSuperAdminPassword() {
        return superAdminPassword;
    }

    public static String getFirstAdminPassword() {
        return firstAdminPassword;
    }

    public static String getSuperAdminLogin() {
        return superAdminLogin;
    }

    public static String getFirstAdminLogin() {
        return firstAdminLogin;
    }
}

package team.exlab.ecohub;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import team.exlab.ecohub.recyclingpoint.RecyclingPointMapper;
import team.exlab.ecohub.recyclingpoint.dto.RecyclingPointDto;
import team.exlab.ecohub.recyclingpoint.model.WorkingHours;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
public class EcohubApplication {
    private static String superAdminPassword;
    private static String firstAdminPassword;

    public static void main(String[] args) {
//        superAdminPassword = args[0];
//        firstAdminPassword = args[1];
        SpringApplication.run(EcohubApplication.class, args);
    }

    public static String getSuperAdminPassword() {
        return superAdminPassword;
    }

    public static String getFirstAdminPassword() {
        return firstAdminPassword;
    }
}

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
    public static void main(String[] args) {
        SpringApplication.run(EcohubApplication.class, args);
    }
}

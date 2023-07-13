package team.exlab.ecohub;

import lombok.RequiredArgsConstructor;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import org.springframework.boot.test.json.ObjectContent;
import team.exlab.ecohub.recyclingpoint.RecyclingPointMapper;
import team.exlab.ecohub.recyclingpoint.dto.RecyclingPointDto;
import team.exlab.ecohub.recyclingpoint.dto.RecyclingPointPartInfoDto;
import team.exlab.ecohub.recyclingpoint.model.*;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.Set;

@JsonTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class RecyclingPointSerializationTests {
    private final JacksonTester<RecyclingPointDto> jsonFullDto;
    private final JacksonTester<RecyclingPointPartInfoDto> jsonPartDto;
    private final JacksonTester<RecyclingPoint> pointObject;

    private static Map<DayOfWeek, WorkingHours> workingHours;
    private RecyclingPoint point;
    private RecyclingPointDto pointDto;
    private RecyclingPointPartInfoDto pointPartInfoDto;

    @BeforeAll
    static void beforeAll() {
        WorkingHours dailyWorkingHours = new WorkingHours(LocalTime.of(9, 0),
                LocalTime.of(17, 0),
                LocalTime.of(12, 0),
                LocalTime.of(13, 0));
        workingHours = Map.of(DayOfWeek.MONDAY, dailyWorkingHours,
                DayOfWeek.TUESDAY, dailyWorkingHours,
                DayOfWeek.WEDNESDAY, dailyWorkingHours,
                DayOfWeek.THURSDAY, dailyWorkingHours,
                DayOfWeek.FRIDAY, dailyWorkingHours,
                DayOfWeek.SATURDAY, dailyWorkingHours,
                DayOfWeek.SUNDAY, dailyWorkingHours);
    }

    @BeforeEach
    void beforeEach() {
        point = new RecyclingPoint(
                1L,
                "pointName",
                "pointAddress",
                "pointPhoneNumber",
                "pointWebsite",
                new Location(0.0, 0.0),
                workingHours,
                Set.of(new RecyclableType(1L, ERecyclableType.METAL)));
        pointDto = RecyclingPointMapper.toDto(point);
        pointPartInfoDto = RecyclingPointMapper.toPartInfoDto(point);
    }

    @Test
    void recyclingPointDto() throws Exception {
        JsonContent<RecyclingPointDto> result = jsonFullDto.write(pointDto);

        Assertions.assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        Assertions.assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo("pointName");
        Assertions.assertThat(result).extractingJsonPathStringValue("$.address").isEqualTo("pointAddress");
        Assertions.assertThat(result).extractingJsonPathStringValue("$.phoneNumber").isEqualTo("pointPhoneNumber");
        Assertions.assertThat(result).extractingJsonPathStringValue("$.website").isEqualTo("pointWebsite");
        Assertions.assertThat(result).extractingJsonPathNumberValue("$.location.latitude").isEqualTo(0.0);
        Assertions.assertThat(result).extractingJsonPathNumberValue("$.location.longitude").isEqualTo(0.0);
        Assertions.assertThat(result).extractingJsonPathStringValue("$.workingHours.MONDAY.openingTime").isEqualTo("09:00");
        Assertions.assertThat(result).extractingJsonPathStringValue("$.workingHours.THURSDAY.lunchStartTime").isEqualTo("12:00");
        Assertions.assertThat(result).extractingJsonPathArrayValue("$.recyclableTypes").isEqualTo(List.of("METAL"));

    }

    @Test
    void recyclingPointPartInfoDto() throws Exception {
        JsonContent<RecyclingPointPartInfoDto> result = jsonPartDto.write(pointPartInfoDto);

        Assertions.assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        Assertions.assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo("pointName");
        Assertions.assertThat(result).extractingJsonPathStringValue("$.address").isEqualTo("pointAddress");
        Assertions.assertThat(result).extractingJsonPathStringValue("$.phoneNumber").isEqualTo("pointPhoneNumber");
        Assertions.assertThat(result).extractingJsonPathStringValue("$.website").isEqualTo("pointWebsite");
    }

    @Test
    void recyclingPointFromPartDto() throws Exception{
        JsonContent<RecyclingPointPartInfoDto> result = jsonPartDto.write(pointPartInfoDto);

        ObjectContent<RecyclingPoint> objectResult = pointObject.parse(result.getJson());
        Assertions.assertThat(objectResult).hasFieldOrPropertyWithValue("id", point.getId());
        Assertions.assertThat(objectResult).hasFieldOrPropertyWithValue("name", point.getName());
        Assertions.assertThat(objectResult).hasFieldOrPropertyWithValue("address", point.getAddress());
        Assertions.assertThat(objectResult).hasFieldOrPropertyWithValue("phoneNumber", point.getPhoneNumber());
        Assertions.assertThat(objectResult).hasFieldOrPropertyWithValue("website", point.getWebsite());

    }
}

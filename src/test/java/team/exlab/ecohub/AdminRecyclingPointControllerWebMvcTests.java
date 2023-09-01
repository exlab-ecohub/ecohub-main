package team.exlab.ecohub;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import team.exlab.ecohub.recyclingpoint.RecyclingPointMapper;
import team.exlab.ecohub.recyclingpoint.RecyclingPointService;
import team.exlab.ecohub.recyclingpoint.controller.AdminRecyclingPointController;
import team.exlab.ecohub.recyclingpoint.dto.RecyclingPointDto;
import team.exlab.ecohub.recyclingpoint.model.*;

import java.nio.charset.StandardCharsets;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Map;
import java.util.Set;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = AdminRecyclingPointController.class)
class AdminRecyclingPointControllerWebMvcTests {
    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private MockMvc mvc;
    @MockBean
    private RecyclingPointService service;

    private static Map<DayOfWeek, WorkingHours> workingHours;
    private RecyclingPoint point;
    private RecyclingPointDto pointDto;

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

//    @BeforeEach
//    void beforeEach() {
//        point = new RecyclingPoint(
//                1L,
//                "pointName",
//                "pointAddress",
//                "pointPhoneNumber",
//                "pointWebsite",
//                new Location(0.0, 0.0),
//                workingHours,
//                Set.of(new RecyclableType(1L, ERecyclableType.METAL)));
//        pointDto = RecyclingPointMapper.toDto(point);
//    }

    @Test
    void createPoint() throws Exception {
        when(service.createPoint(any())).thenReturn(pointDto);
        mvc.perform(post("/admin/recycling-points")
                        .content(mapper.writeValueAsString(pointDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(pointDto)));
    }

    @Test
    void updatePoint() throws Exception {
        var pointDtoUpdated = pointDto;
        pointDtoUpdated.setName("updatedPointName");

        when(service.updatePoint(anyLong(), any())).thenReturn(pointDtoUpdated);
        mvc.perform(patch("/admin/recycling-points/{pointId}", point.getId())
                        .content(mapper.writeValueAsString(pointDtoUpdated))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(pointDtoUpdated)));
    }

    @Test
    void deletePoint() throws Exception {
        mvc.perform(delete("/admin/recycling-points/{pointId}", point.getId())
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isOk());
        Mockito.verify(service, Mockito.times(1)).deletePoint(point.getId());
    }
}

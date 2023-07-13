package team.exlab.ecohub;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import team.exlab.ecohub.exception.RecyclingPointNotFoundException;
import team.exlab.ecohub.recyclingpoint.RecyclingPointMapper;
import team.exlab.ecohub.recyclingpoint.RecyclingPointService;
import team.exlab.ecohub.recyclingpoint.controller.RecyclingPointController;
import team.exlab.ecohub.recyclingpoint.dto.RecyclingPointDto;
import team.exlab.ecohub.recyclingpoint.dto.RecyclingPointPartInfoDto;
import team.exlab.ecohub.recyclingpoint.model.*;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = RecyclingPointController.class)
class RecyclingPointControllerWebMvcTests {
    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private MockMvc mvc;
    @MockBean
    private RecyclingPointService service;

    private static Map<DayOfWeek, WorkingHours> workingHours;
    private RecyclingPoint point;
    private RecyclingPointDto pointDto;
    private RecyclingPointPartInfoDto pointPartDto;

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
        pointPartDto = RecyclingPointMapper.toPartInfoDto(point);
    }

    @Test
    void getPoints() throws Exception {
        when(service.getPoints(any(), anyInt(), anyInt())).thenReturn(List.of(pointPartDto));
        mvc.perform(get("/recycling-points")
                        /*get status 302 without it. Although the endpoint does not require authentication.
                        Do not understand how to fix it in test */
                        .with(SecurityMockMvcRequestPostProcessors.jwt())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(List.of(pointPartDto))));
    }

    @Test
    void getPointReturnsStatus200AndPointDtoWhenPointExists() throws Exception {
        when(service.getPoint(anyLong())).thenReturn(pointDto);
        mvc.perform(get("/recycling-points/{pointId}", point.getId())
                        /*get status 302 without it. Although the endpoint does not require authentication.
                        Do not understand how to fix it in test */
                        .with(SecurityMockMvcRequestPostProcessors.jwt())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(pointDto)));
    }

    @Test
    void getPointReturnStatus404WhenPointNotFound() throws Exception {
        when(service.getPoint(anyLong())).thenThrow(RecyclingPointNotFoundException.class);
        mvc.perform(get("/recycling-points/{pointId}", point.getId())
                        /*get status 302 without it. Although the endpoint does not require authentication.
                        Do not understand how to fix it in test */
                        .with(SecurityMockMvcRequestPostProcessors.jwt())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}

package team.exlab.ecohub.recyclingpoint.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import team.exlab.ecohub.recyclingpoint.model.Location;
import team.exlab.ecohub.recyclingpoint.model.WorkingHours;

import java.time.DayOfWeek;
import java.util.Map;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class RecyclingPointDto {
    private Long id;
    private String name;
    private String address;
    private String phoneNumber;
    private String website;
    private Location location;
    private Map<DayOfWeek, WorkingHours> workingHours;
    private Set<String> recyclableTypes;
}

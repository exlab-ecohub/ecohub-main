package team.exlab.ecohub.recyclingpoint.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import team.exlab.ecohub.recyclingpoint.model.Location;
import team.exlab.ecohub.recyclingpoint.model.WorkingHours;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Getter
@Setter
@AllArgsConstructor
@Builder

public class RecyclingPointDto {
    private Long id;
    @NotBlank
    @Size(min = 3, max = 100, message = "Name must be between 3 and 100 characters long")
    private String name;
    @NotBlank
    @Size(min = 3, max = 100, message = "Address must be between 3 and 100 characters long")
    private String address;
    @Size(min = 4, max = 100, message = "Phone number must be between 4 and 100 characters long")
    private String phoneNumber;
    @Size(min = 4, max = 100, message = "Website must be between 4 and 100 characters long")
    private String website;
    @NotNull(message = "GPS coordinates required")
    private Location location;
    private String workingHoursAsString;
    @NotEmpty(message = "At least one recyclable type required")
    private Set<String> recyclableTypes;
    private boolean displayed;



}

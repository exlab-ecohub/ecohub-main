package team.exlab.ecohub.recyclingpoint.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Embeddable;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
@EqualsAndHashCode
public class WorkingHours {
    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME, pattern = "HH:mm:ss")
    @JsonFormat(pattern = "HH:mm")
    private LocalTime openingTime;
    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME, pattern = "HH:mm:ss")
    @JsonFormat(pattern = "HH:mm")
    private LocalTime closingTime;
    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME, pattern = "HH:mm:ss")
    @JsonFormat(pattern = "HH:mm")
    private LocalTime lunchStartTime;
    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME, pattern = "HH:mm:ss")
    @JsonFormat(pattern = "HH:mm")
    private LocalTime lunchEndTime;
}

package team.exlab.ecohub.recyclingpoint.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Embeddable;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class WorkingHours {
    private LocalTime openingTime;
    private LocalTime closingTime;
    private LocalTime lunchStartTime;
    private LocalTime lunchEndTime;
}

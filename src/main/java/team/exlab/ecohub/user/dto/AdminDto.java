package team.exlab.ecohub.user.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@ToString
public class AdminDto {
    private Long id;
    private String username;
    private int passAttempts;
    private boolean blockedBefore;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime lockEndTime;
}

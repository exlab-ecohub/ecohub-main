package team.exlab.ecohub.feedback.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import team.exlab.ecohub.feedback.ResponseStatus;

import java.time.LocalDateTime;
@Getter
@Setter
@Builder
@AllArgsConstructor
public class FeedbackUserDto {
    private String email;
    private String messageTopic;
    private String messageContent;
    private LocalDateTime messageTime;
    private String responseContent;
    private ResponseStatus responseStatus;
    private LocalDateTime responseTime;
}

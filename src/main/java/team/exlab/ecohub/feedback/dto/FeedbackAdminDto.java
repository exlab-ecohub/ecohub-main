package team.exlab.ecohub.feedback.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import team.exlab.ecohub.feedback.ResponseStatus;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class FeedbackAdminDto {
    private Long feedbackId;
    private String email;
    private String messageTopic;
    private StringBuilder messageContent;
    private LocalDateTime messageTime;
    private StringBuilder responseContent;
    private LocalDateTime responseTime;
    private ResponseStatus responseStatus;
}

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
public class FeedbackAdminDto {
    private Long feedbackId;
    private String email;
    private String messageTopic;
    private String messageContent;
    private LocalDateTime messageTime;
    private String responseContent;
    private LocalDateTime responseTime;
    private ResponseStatus responseStatus;
}

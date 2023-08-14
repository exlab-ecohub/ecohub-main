package team.exlab.ecohub.feedback.dto;

import lombok.Builder;
import lombok.Data;
import team.exlab.ecohub.feedback.ResponseStatus;

import java.time.LocalDateTime;

@Data
@Builder
public class FeedbackAdminDto {
    private Long feedback_id;
    private String email;
    private String message_topic;
    private StringBuilder message_content;
    private LocalDateTime message_time;
    private StringBuilder response_content;
    private LocalDateTime response_time;
    private ResponseStatus response_status;
}

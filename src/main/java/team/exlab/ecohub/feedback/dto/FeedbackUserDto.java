package team.exlab.ecohub.feedback.dto;

import lombok.Builder;
import lombok.Data;
import team.exlab.ecohub.feedback.ResponseStatus;

import java.time.LocalDateTime;
@Data
@Builder
public class FeedbackUserDto {
    private String message_topic;
    private StringBuilder message_content;
    private LocalDateTime message_time;
    private StringBuilder response_content;
    private ResponseStatus response_status;
    private LocalDateTime response_time;
}

package team.exlab.ecohub.feedback.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import team.exlab.ecohub.feedback.model.MessageTopic;
import team.exlab.ecohub.feedback.model.ResponseStatus;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class FeedbackAdminDto {
    private Long id;
    private int userFeedbackCount;
    private String name;
    private String email;
    private MessageTopic messageTopic;
    private String messageContent;
    private LocalDateTime messageTime;
    private String responseContent;
    private LocalDateTime responseTime;
    private ResponseStatus responseStatus;
}

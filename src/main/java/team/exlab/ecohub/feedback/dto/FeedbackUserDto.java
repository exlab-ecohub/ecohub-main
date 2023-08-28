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
@Builder
@AllArgsConstructor
public class FeedbackUserDto {
    private String name;
    private int userFeedbackCount;
    private String email;
    private MessageTopic messageTopic;
    private String messageContent;
    private LocalDateTime messageTime;
    private String responseContent;
    private LocalDateTime responseTime;
    private ResponseStatus responseStatus;
    //    private Set<MessageAndResponse> messagesAndResponses;
}

package team.exlab.ecohub.feedback.dto;

import org.springframework.stereotype.Component;
import team.exlab.ecohub.feedback.Feedback;

@Component
public class FeedbackUserMapper {
    public static FeedbackUserDto toDto(Feedback feedback) {
        return FeedbackUserDto.builder().
                message_topic(feedback.getMessageTopic()).
                message_content(feedback.getMessageContent()).
                message_time(feedback.getMessageTime()).
                response_content(feedback.getResponseContent()).
                response_time(feedback.getResponseTime()).
                response_status(feedback.getResponseStatus()).
                build();
    }

    public static Feedback toFeedback(FeedbackUserDto feedbackUserDto) {
        return Feedback.builder().messageTopic(feedbackUserDto.getMessage_topic()).messageContent(feedbackUserDto.getMessage_content()).
                messageTime(feedbackUserDto.getMessage_time()).
                responseContent(feedbackUserDto.getResponse_content()).
                responseTime(feedbackUserDto.getResponse_time()).
                responseStatus(feedbackUserDto.getResponse_status()).
                build();
    }
}

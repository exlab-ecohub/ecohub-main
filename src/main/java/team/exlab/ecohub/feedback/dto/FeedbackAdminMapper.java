package team.exlab.ecohub.feedback.dto;

import org.springframework.stereotype.Component;
import team.exlab.ecohub.feedback.Feedback;

@Component
public class FeedbackAdminMapper {
    public static FeedbackAdminDto toDto(Feedback feedback) {
        return FeedbackAdminDto.builder().
                feedback_id(feedback.getFeedbackId()).
                email(feedback.getEmail()).
                message_topic(feedback.getMessageTopic()).
                message_content(feedback.getMessageContent()).
                message_time(feedback.getMessageTime()).
                response_content(feedback.getResponseContent()).
                response_time(feedback.getResponseTime()).
                response_status(feedback.getResponseStatus()).
                build();
    }

    public static Feedback toFeedback(FeedbackAdminDto feedbackAdminDto) {
        return Feedback.builder().
                feedbackId(feedbackAdminDto.getFeedback_id()).
                email(feedbackAdminDto.getEmail()).messageTopic(feedbackAdminDto.getMessage_topic()).messageContent(feedbackAdminDto.getMessage_content()).
                messageTime(feedbackAdminDto.getMessage_time()).
                responseContent(feedbackAdminDto.getResponse_content()).
                responseTime(feedbackAdminDto.getResponse_time()).
                responseStatus(feedbackAdminDto.getResponse_status()).
                build();
    }
}

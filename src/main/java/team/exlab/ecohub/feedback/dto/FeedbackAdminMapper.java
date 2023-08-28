package team.exlab.ecohub.feedback.dto;

import org.springframework.stereotype.Component;
import team.exlab.ecohub.feedback.model.Feedback;

@Component
public class FeedbackAdminMapper {
    public static FeedbackAdminDto toDto(Feedback feedback) {
        return FeedbackAdminDto.builder()
                .id(feedback.getId())
                .userFeedbackCount(feedback.getUserFeedbackCount())
                .name(feedback.getName())
                .email(feedback.getEmail())
                .messageTopic(feedback.getMessageTopic())
                .messageContent(feedback.getMessageContent())
                .messageTime(feedback.getMessageTime())
                .responseContent(feedback.getResponseContent())
                .responseTime(feedback.getResponseTime())
                .responseStatus(feedback.getResponseStatus())
                .build();
    }

    public static Feedback toFeedback(FeedbackAdminDto feedbackAdminDto) {
        return Feedback.builder()
                .id(feedbackAdminDto.getId())
                .userFeedbackCount(feedbackAdminDto.getUserFeedbackCount())
                .name(feedbackAdminDto.getName())
                .email(feedbackAdminDto.getEmail())
                .messageTopic(feedbackAdminDto.getMessageTopic())
                .messageContent(feedbackAdminDto.getMessageContent())
                .messageTime(feedbackAdminDto.getMessageTime())
                .responseContent(feedbackAdminDto.getResponseContent())
                .responseTime(feedbackAdminDto.getResponseTime())
                .responseStatus(feedbackAdminDto.getResponseStatus())
                .build();
    }
}

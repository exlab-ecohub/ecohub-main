package team.exlab.ecohub.feedback.dto;

import org.springframework.stereotype.Component;
import team.exlab.ecohub.feedback.Feedback;

@Component
public class FeedbackAdminMapper {
    public static FeedbackAdminDto toDto(Feedback feedback) {
        return FeedbackAdminDto.builder().
                feedback_id(feedback.getFeedback_id()).
                email(feedback.getEmail()).
                message_topic(feedback.getMessage_topic()).
                message_content(feedback.getMessage_content()).
                message_time(feedback.getMessage_time()).
                response_content(feedback.getResponse_content()).
                response_time(feedback.getResponse_time()).
                response_status(feedback.getResponse_status()).
                build();
    }

    public static Feedback toFeedback(FeedbackAdminDto feedbackAdminDto) {
        return Feedback.builder().
                feedback_id(feedbackAdminDto.getFeedback_id()).
                email(feedbackAdminDto.getEmail()).
                message_topic(feedbackAdminDto.getMessage_topic()).
                message_content(feedbackAdminDto.getMessage_content()).
                message_time(feedbackAdminDto.getMessage_time()).
                response_content(feedbackAdminDto.getResponse_content()).
                response_time(feedbackAdminDto.getResponse_time()).
                response_status(feedbackAdminDto.getResponse_status()).
                build();
    }
}

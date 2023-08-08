package team.exlab.ecohub.feedback.dto;

import org.springframework.stereotype.Component;
import team.exlab.ecohub.feedback.Feedback;

@Component
public class FeedbackUserMapper {
    public static FeedbackUserDto toDto(Feedback feedback) {
        return FeedbackUserDto.builder().
                message_topic(feedback.getMessage_topic()).
                message_content(feedback.getMessage_content()).
                message_time(feedback.getMessage_time()).
                response_content(feedback.getResponse_content()).
                response_time(feedback.getResponse_time()).
                response_status(feedback.getResponse_status()).
                build();
    }

    public static Feedback toFeedback(FeedbackUserDto feedbackUserDto) {
        return Feedback.builder().
                message_topic(feedbackUserDto.getMessage_topic()).
                message_content(feedbackUserDto.getMessage_content()).
                message_time(feedbackUserDto.getMessage_time()).
                response_content(feedbackUserDto.getResponse_content()).
                response_time(feedbackUserDto.getResponse_time()).
                response_status(feedbackUserDto.getResponse_status()).
                build();
    }
}

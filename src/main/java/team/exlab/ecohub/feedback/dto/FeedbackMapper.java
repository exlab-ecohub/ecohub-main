package team.exlab.ecohub.feedback.dto;

import team.exlab.ecohub.feedback.model.Feedback;

public class FeedbackMapper {
    private FeedbackMapper() {
    }

    public static FeedbackDto toDto(Feedback feedback) {
        return FeedbackDto.builder()
                .id(feedback.getId())
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

    public static Feedback toFeedback(FeedbackDto feedbackDto) {
        return Feedback.builder()
                .id(feedbackDto.getId())
                .name(feedbackDto.getName())
                .email(feedbackDto.getEmail())
                .messageTopic(feedbackDto.getMessageTopic())
                .messageContent(feedbackDto.getMessageContent())
                .messageTime(feedbackDto.getMessageTime())
                .responseContent(feedbackDto.getResponseContent())
                .responseTime(feedbackDto.getResponseTime())
                .responseStatus(feedbackDto.getResponseStatus())
                .build();
    }
}

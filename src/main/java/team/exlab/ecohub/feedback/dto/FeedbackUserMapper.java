package team.exlab.ecohub.feedback.dto;

import team.exlab.ecohub.feedback.model.Feedback;

public class FeedbackUserMapper {
    public static FeedbackUserDto toDto(Feedback feedback) {
        return FeedbackUserDto.builder().userFeedbackCount(feedback.getUserFeedbackCount()).name(feedback.getName()).email(feedback.getEmail()).messageTopic(feedback.getMessageTopic()).messageContent(feedback.getMessageContent()).messageTime(feedback.getMessageTime()).responseContent(feedback.getResponseContent()).responseTime(feedback.getResponseTime()).responseStatus(feedback.getResponseStatus()).build();
    }

    public static Feedback toFeedback(FeedbackUserDto feedbackUserDto) {
        return Feedback.builder().name(feedbackUserDto.getName()).email(feedbackUserDto.getEmail()).messageTopic(feedbackUserDto.getMessageTopic()).messageContent(feedbackUserDto.getMessageContent()).messageTime(feedbackUserDto.getMessageTime()).responseContent(feedbackUserDto.getResponseContent()).responseTime(feedbackUserDto.getResponseTime()).responseStatus(feedbackUserDto.getResponseStatus()).build();
    }
}

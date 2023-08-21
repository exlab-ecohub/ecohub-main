package team.exlab.ecohub.feedback;

import team.exlab.ecohub.feedback.dto.FeedbackAdminDto;
import team.exlab.ecohub.feedback.dto.FeedbackUserDto;

import java.util.List;

public interface FeedbackService {
    FeedbackUserDto createFeedback(FeedbackUserDto userFeedback);

    FeedbackAdminDto createResponseToFeedback(Feedback feedback, String response);
    List<FeedbackAdminDto> getFeedbacks(ResponseStatus responseStatus);
    List<FeedbackAdminDto> getFeedbacks(ResponseStatus responseStatus, String message_topic);
    FeedbackUserDto getFeedbackAndResponse(FeedbackUserDto feedbackUserDto);
    List<FeedbackUserDto> showFeedbacksForUser(Long userId);



}

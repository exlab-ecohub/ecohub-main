package team.exlab.ecohub.feedback;

import team.exlab.ecohub.feedback.dto.FeedbackAdminDto;
import team.exlab.ecohub.feedback.dto.FeedbackUserDto;

import java.util.List;

public interface FeedbackService {
    FeedbackAdminDto createFeedback(FeedbackUserDto userFeedback);

    FeedbackAdminDto createResponseToFeedback(FeedbackAdminDto feedbackAdminDto);
    List<FeedbackAdminDto> getFeedbacks(ResponseStatus responseStatus);
    List<FeedbackAdminDto> getFeedbacks(ResponseStatus responseStatus, String message_topic);
    FeedbackUserDto getFeedbackAndResponse(FeedbackUserDto feedbackUserDto);



}

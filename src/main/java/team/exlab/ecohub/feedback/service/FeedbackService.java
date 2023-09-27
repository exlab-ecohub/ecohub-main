package team.exlab.ecohub.feedback.service;

import team.exlab.ecohub.feedback.dto.AdminResponseDto;
import team.exlab.ecohub.feedback.dto.FeedbackDto;
import team.exlab.ecohub.feedback.model.MessageTopic;
import team.exlab.ecohub.feedback.model.ResponseStatus;

import java.util.List;

public interface FeedbackService {

    FeedbackDto createResponseToFeedback(Long feedbackId, AdminResponseDto adminResponseDto);

    List<FeedbackDto> getFeedbacks(ResponseStatus responseStatus, MessageTopic messageTopic);

    FeedbackDto getFeedback(Long feedbackId);

    void createFeedback(FeedbackDto userFeedback);

    FeedbackDto getFeedbackForUser(Long userFeedbackCount);

    List<FeedbackDto> getAllFeedbacksForUser();


}

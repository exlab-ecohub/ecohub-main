package team.exlab.ecohub.feedback.service;

import team.exlab.ecohub.feedback.dto.FeedbackDto;
import team.exlab.ecohub.feedback.model.MessageTopic;
import team.exlab.ecohub.feedback.model.ResponseStatus;

import java.util.List;

public interface FeedbackService {

    FeedbackDto createResponseToFeedback(FeedbackDto feedbackDto, Long id);

    List<FeedbackDto> getFeedbacks(ResponseStatus responseStatus, MessageTopic messageTopic);

    FeedbackDto getFeedback(Long feedbackId);

    FeedbackDto createFeedback(FeedbackDto userFeedback);

    FeedbackDto getOneFeedbackForUser(Long userFeedbackCount);

    List<FeedbackDto> getAllFeedbacksForUser();


}

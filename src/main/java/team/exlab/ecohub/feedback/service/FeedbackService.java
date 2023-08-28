package team.exlab.ecohub.feedback.service;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import team.exlab.ecohub.feedback.dto.FeedbackAdminDto;
import team.exlab.ecohub.feedback.dto.FeedbackUserDto;
import team.exlab.ecohub.feedback.model.MessageTopic;
import team.exlab.ecohub.feedback.model.ResponseStatus;

import java.util.List;

public interface FeedbackService {

    EntityModel<FeedbackAdminDto> createResponseToFeedback(FeedbackAdminDto feedbackAdminDto, Long id);

    CollectionModel<EntityModel<FeedbackAdminDto>> getFeedbacks(ResponseStatus responseStatus, MessageTopic messageTopic);

    EntityModel<FeedbackAdminDto> getOneFeedback(Long feedbackId);

    EntityModel<FeedbackUserDto> createFeedback(FeedbackUserDto userFeedback);

    EntityModel<FeedbackUserDto> getOneFeedbackForUser(Integer userFeedbackCount);

    CollectionModel<EntityModel<FeedbackUserDto>> getAllFeedbacksForUser();


}

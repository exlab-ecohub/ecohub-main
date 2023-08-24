package team.exlab.ecohub.feedback;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import team.exlab.ecohub.feedback.dto.FeedbackUserDto;

import java.util.List;

public interface FeedbackService {
    FeedbackUserDto createFeedback(FeedbackUserDto userFeedback);

    Feedback createResponseToFeedback(Feedback feedback, String response);
    CollectionModel<EntityModel<Feedback>> getFeedbacks(ResponseStatus responseStatus, MessageTopic messageTopic);
//    CollectionModel<EntityModel<Feedback>> getFeedbacksByStatus(ResponseStatus responseStatus);
//    CollectionModel<EntityModel<Feedback>> getFeedbacksByStatusAndTopic(ResponseStatus responseStatus, MessageTopic messageTopic);
    FeedbackUserDto getOneFeedbackForUser(Long feedbackId);
    List<FeedbackUserDto> getAllFeedbacksForUser();



}

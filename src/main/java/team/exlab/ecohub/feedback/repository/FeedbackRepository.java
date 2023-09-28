package team.exlab.ecohub.feedback.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import team.exlab.ecohub.feedback.model.Feedback;
import team.exlab.ecohub.feedback.model.MessageTopic;
import team.exlab.ecohub.feedback.model.ResponseStatus;

import java.util.List;
import java.util.Optional;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
    List<Feedback> findAllByResponseStatusOrderById(ResponseStatus responseStatus);

    List<Feedback> findAllByMessageTopicOrderById(MessageTopic messageTopic);

    List<Feedback> findAllByResponseStatusAndMessageTopicOrderById(ResponseStatus responseStatus, MessageTopic messageTopic);

    List<Feedback> findAllByEmailOrderById(String email);

    Optional<Feedback> findFeedbackByEmailAndId(String email, Long feedbackId);
}

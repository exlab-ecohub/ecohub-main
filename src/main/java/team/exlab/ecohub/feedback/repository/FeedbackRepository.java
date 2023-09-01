package team.exlab.ecohub.feedback.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import team.exlab.ecohub.feedback.model.Feedback;
import team.exlab.ecohub.feedback.model.MessageTopic;
import team.exlab.ecohub.feedback.model.ResponseStatus;
import team.exlab.ecohub.user.model.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
    List<Feedback> findAllByResponseStatus(ResponseStatus responseStatus);

    List<Feedback> findAllByMessageTopic(MessageTopic messageTopic);

    List<Feedback> findAllByResponseStatusAndMessageTopic(ResponseStatus responseStatus, MessageTopic messageTopic);

    List<Feedback> findAllByEmail(String email);
    Optional<Feedback> findFirstByEmail(String email);

    int countFeedbackByEmail(String email);

    Feedback findFeedbackByUserFeedbackCountAndUser(Integer userFeedbackCount, User users);

}

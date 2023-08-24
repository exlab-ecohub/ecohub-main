package team.exlab.ecohub.feedback;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import team.exlab.ecohub.feedback.dto.FeedbackUserDto;

import java.util.List;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
    List<Feedback> findAllByResponseStatus(ResponseStatus responseStatus);
    List<Feedback> findAllByMessageTopic(MessageTopic messageTopic);
    List<Feedback> findAllByResponseStatusAndMessageTopic(ResponseStatus responseStatus, MessageTopic messageTopic);
    List<FeedbackUserDto> findAllByEmail(String email);
    Feedback findFeedbackByFeedbackIdAndEmail(Long feedbackId, String email);
    Feedback findFeedbackByUserIdAndFeedbackId(Long userId, Long feedbackId);

}

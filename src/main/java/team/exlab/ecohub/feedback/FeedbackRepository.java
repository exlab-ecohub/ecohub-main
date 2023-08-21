package team.exlab.ecohub.feedback;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import team.exlab.ecohub.feedback.dto.FeedbackUserDto;

import java.util.List;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
    public List<FeedbackUserDto> findAllByEmailIs(String email);
}

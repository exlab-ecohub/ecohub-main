package team.exlab.ecohub.feedback;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.exlab.ecohub.feedback.dto.FeedbackAdminDto;
import team.exlab.ecohub.feedback.dto.FeedbackAdminMapper;
import team.exlab.ecohub.feedback.dto.FeedbackUserDto;
import team.exlab.ecohub.feedback.dto.FeedbackUserMapper;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FeedbackServiceImpl implements FeedbackService {
    private final FeedbackRepository repository;

    @Override
    @Transactional
    public FeedbackUserDto createFeedback(FeedbackUserDto userFeedback) {
        Feedback feedbackToSave = FeedbackUserMapper.toFeedback(userFeedback);
        feedbackToSave.setResponseStatus(ResponseStatus.OPEN);
        return FeedbackUserMapper.toDto(repository.save(feedbackToSave));
    }

    @Override
    public FeedbackAdminDto createResponseToFeedback(Feedback feedback, String response) {
        Long feedback_id = feedback.getFeedbackId();
        Feedback feedbackToAlter = repository.findById(feedback_id).orElseThrow(() -> new FeedbackNotFoundException(feedback_id));
        String feedbackResponseContent = String.join(feedbackToAlter.getResponseContent(), response);
        feedbackToAlter.setResponseStatus(ResponseStatus.IN_PROGRESS);
        feedbackToAlter.setResponseContent(feedbackResponseContent);
        feedbackToAlter.setResponseTime(LocalDateTime.now());
        return FeedbackAdminMapper.toDto(feedbackToAlter);
    }

    @Override
    public List<FeedbackAdminDto> getFeedbacks(ResponseStatus responseStatus) {
        return null;
    }

    @Override
    public List<FeedbackAdminDto> getFeedbacks(ResponseStatus responseStatus, String message_topic) {
        return null;
    }

    @Override
    public FeedbackUserDto getFeedbackAndResponse(FeedbackUserDto feedbackUserDto) {
        return null;
    }


    @Override
    public List<FeedbackUserDto> showFeedbacksForUser(Long userId) {
        String email = repository.findById(userId).orElseThrow().getEmail();
        return repository.findAllByEmailIs(email);
    }
}

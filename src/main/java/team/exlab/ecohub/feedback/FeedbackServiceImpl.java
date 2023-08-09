package team.exlab.ecohub.feedback;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.exlab.ecohub.feedback.dto.FeedbackAdminDto;
import team.exlab.ecohub.feedback.dto.FeedbackAdminMapper;
import team.exlab.ecohub.feedback.dto.FeedbackUserDto;
import team.exlab.ecohub.feedback.dto.FeedbackUserMapper;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class FeedbackServiceImpl implements FeedbackService {
    private final FeedbackRepository repository;

    public FeedbackServiceImpl(FeedbackRepository repository) {
        this.repository = repository;
    }


    @Override
    @Transactional
    public FeedbackAdminDto createFeedback(FeedbackUserDto userFeedback) {
        Feedback feedbackToSave = FeedbackUserMapper.toFeedback(userFeedback);
        feedbackToSave.setResponse_status(ResponseStatus.OPEN);
        return FeedbackAdminMapper.toDto(repository.save(feedbackToSave));
    }

    @Override
    public FeedbackAdminDto createResponseToFeedback(FeedbackAdminDto feedbackAdminDto) {
        String response = feedbackAdminDto.getMessage_content();
        return addResponseToMessage(feedbackAdminDto, response);
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

    public FeedbackAdminDto addResponseToMessage(FeedbackAdminDto feedbackAdminDto, String response) {
        Long feedback_id = feedbackAdminDto.getFeedback_id();
        Feedback feedbackToAlter = repository.findById(feedback_id).orElseThrow(() -> new FeedbackNotFoundException(feedback_id));
        StringBuilder sb = new StringBuilder(feedbackToAlter.getMessage_content());
        sb.append("\n\n\n").append(LocalDateTime.now()).append("\n").append(response);
        feedbackToAlter.setResponse_status(ResponseStatus.IN_PROGRESS);
        feedbackToAlter.setMessage_content(sb);
        return FeedbackAdminMapper.toDto(feedbackToAlter);
    }
}

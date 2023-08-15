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
    public FeedbackAdminDto createResponseToFeedback(FeedbackAdminDto feedbackAdminDto) {
        StringBuilder response = feedbackAdminDto.getMessageContent();
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

    public FeedbackAdminDto addResponseToMessage(FeedbackAdminDto feedbackAdminDto, StringBuilder response) {
        Long feedback_id = feedbackAdminDto.getFeedbackId();
        Feedback feedbackToAlter = repository.findById(feedback_id).orElseThrow(() -> new FeedbackNotFoundException(feedback_id));
        StringBuilder sb = new StringBuilder(feedbackToAlter.getMessageContent());
        sb.append("\n\n\n").append(LocalDateTime.now()).append("\n").append(response);
        feedbackToAlter.setResponseStatus(ResponseStatus.IN_PROGRESS);
        feedbackToAlter.setMessageContent(sb);
        return FeedbackAdminMapper.toDto(feedbackToAlter);
    }
}

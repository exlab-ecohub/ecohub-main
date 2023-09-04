package team.exlab.ecohub.feedback.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.exlab.ecohub.exception.FeedbackNotFoundException;
import team.exlab.ecohub.feedback.dto.FeedbackMapper;
import team.exlab.ecohub.feedback.dto.FeedbackDto;
import team.exlab.ecohub.feedback.model.Feedback;
import team.exlab.ecohub.feedback.model.MessageTopic;
import team.exlab.ecohub.feedback.model.ResponseStatus;
import team.exlab.ecohub.feedback.repository.FeedbackRepository;
import team.exlab.ecohub.user.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FeedbackServiceImpl implements FeedbackService {
    private final FeedbackRepository feedbackRepository;

    //Methods that are available for admin
    @Override
    @Transactional
    public List<FeedbackDto> getFeedbacks(ResponseStatus status, MessageTopic topic) {
        String defaultValue = "DEFAULT";
        if (!Objects.equals(status.name(), defaultValue) && Objects.equals(topic.name(), defaultValue)) {
            return getFeedbacksByStatus(status);
        } else if (Objects.equals(status.name(), defaultValue) && !Objects.equals(topic.name(), defaultValue)) {
            return getFeedbacksByMessageTopic(topic);
        } else if (!Objects.equals(status.name(), defaultValue) && !Objects.equals(topic.name(), defaultValue)) {
            return getFeedbacksByStatusAndTopic(status, topic);
        } else {
            return getAllFeedbacks();
        }
    }

    @Override
    @Transactional
    public FeedbackDto getFeedback(Long feedbackId) {
        Feedback feedback = feedbackRepository.findById(feedbackId).orElseThrow(() -> new FeedbackNotFoundException(feedbackId));
        return FeedbackMapper.toDto(feedback);
    }

    @Override
    @Transactional
    public FeedbackDto createResponseToFeedback(FeedbackDto feedbackDto, Long feedbackId) {
        User admin = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Feedback feedbackForAnswer = feedbackRepository.findById(feedbackId).orElseThrow(() -> new FeedbackNotFoundException(feedbackId));
        feedbackForAnswer.setAdmin(admin);
        feedbackForAnswer.setResponseStatus(ResponseStatus.CLOSED);
        feedbackForAnswer.setResponseContent(feedbackDto.getResponseContent());
        feedbackForAnswer.setResponseTime(LocalDateTime.now());
        return FeedbackMapper.toDto(feedbackForAnswer);
    }

    //Methods designated for users
    @Override
    @Transactional
    public FeedbackDto createFeedback(FeedbackDto feedbackUserDto) {
        Feedback feedbackForSave = FeedbackMapper.toFeedback(feedbackUserDto);
        Optional<Feedback> feedback = feedbackRepository.findFirstByEmail(feedbackUserDto.getEmail());
        feedback.ifPresent(feedbackFromDb -> feedbackForSave.setUser(feedbackFromDb.getUser()));
        if (Objects.equals(feedbackUserDto.getMessageTopic(), null)) {
            feedbackForSave.setMessageTopic(MessageTopic.DEFAULT);
        }
        feedbackForSave.setMessageContent(feedbackUserDto.getMessageContent());
        feedbackForSave.setResponseStatus(ResponseStatus.OPEN);
        feedbackForSave.setMessageTime(LocalDateTime.now());
        return FeedbackMapper.toDto(feedbackRepository.save(feedbackForSave));
    }

    @Override
    @Transactional
    public FeedbackDto getOneFeedbackForUser(Long feedbackId) {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Feedback feedback = feedbackRepository.findFeedbackByUserIdAndId(currentUser.getId(), feedbackId).orElseThrow(() -> new FeedbackNotFoundException(feedbackId));
        return FeedbackMapper.toDto(feedback);
    }

    @Override
    @Transactional
    public List<FeedbackDto> getAllFeedbacksForUser() {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return feedbackRepository.findAllByEmail(currentUser.getEmail()).stream().map(FeedbackMapper::toDto).collect(Collectors.toList());
    }


    //Supplementary methods
    private List<FeedbackDto> getAllFeedbacks() {
        return feedbackRepository.findAll().stream().map(FeedbackMapper::toDto).collect(Collectors.toList());
    }

    private List<FeedbackDto> getFeedbacksByStatus(ResponseStatus status) {
        return feedbackRepository.findAllByResponseStatus(status).stream().map(FeedbackMapper::toDto).collect(Collectors.toList());
    }

    private List<FeedbackDto> getFeedbacksByMessageTopic(MessageTopic topic) {
        return feedbackRepository.findAllByMessageTopic(topic).stream().map(FeedbackMapper::toDto).collect(Collectors.toList());
    }

    private List<FeedbackDto> getFeedbacksByStatusAndTopic(ResponseStatus status, MessageTopic topic) {
        return feedbackRepository.findAllByResponseStatusAndMessageTopic(status, topic).stream().map(FeedbackMapper::toDto).collect(Collectors.toList());
    }

}

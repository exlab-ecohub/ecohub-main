package team.exlab.ecohub.feedback.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.exlab.ecohub.exception.FeedbackNotFoundException;
import team.exlab.ecohub.feedback.dto.AdminResponseDto;
import team.exlab.ecohub.feedback.dto.FeedbackDto;
import team.exlab.ecohub.feedback.dto.FeedbackMapper;
import team.exlab.ecohub.feedback.model.Feedback;
import team.exlab.ecohub.feedback.model.MessageTopic;
import team.exlab.ecohub.feedback.model.ResponseStatus;
import team.exlab.ecohub.feedback.repository.FeedbackRepository;
import team.exlab.ecohub.user.model.User;
import team.exlab.ecohub.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FeedbackServiceImpl implements FeedbackService {
    private final FeedbackRepository feedbackRepository;
    private final UserRepository userRepository;

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
    public FeedbackDto createResponseToFeedback(Long feedbackId, AdminResponseDto adminResponseDto) {
        User admin = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Feedback feedbackForAnswer = feedbackRepository.findById(feedbackId).orElseThrow(() -> new FeedbackNotFoundException(feedbackId));
        feedbackForAnswer.setAdmin(admin);
        feedbackForAnswer.setResponseStatus(ResponseStatus.CLOSED);
        feedbackForAnswer.setResponseContent(adminResponseDto.getResponseContent());
        feedbackForAnswer.setResponseTime(LocalDateTime.now());
        return FeedbackMapper.toDto(feedbackForAnswer);
    }

    //Methods designated for users
    @Override
    @Transactional
    public void createFeedback(FeedbackDto feedbackUserDto) {
        Feedback feedbackForSave = FeedbackMapper.toFeedback(feedbackUserDto);
        userRepository.findUserByEmail(feedbackUserDto.getEmail())
                .ifPresent(feedbackForSave::setUser);
        if (feedbackUserDto.getMessageTopic() == null) {
            feedbackForSave.setMessageTopic(MessageTopic.DEFAULT);
        }
        feedbackForSave.setMessageContent(feedbackUserDto.getMessageContent());
        feedbackForSave.setResponseStatus(ResponseStatus.OPEN);
        feedbackForSave.setMessageTime(LocalDateTime.now());
        feedbackRepository.save(feedbackForSave);
    }

    @Override
    @Transactional
    public FeedbackDto getFeedbackForUser(Long feedbackId) {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Feedback feedback = feedbackRepository.findFeedbackByEmailAndId(currentUser.getEmail(), feedbackId).orElseThrow(() -> new FeedbackNotFoundException(feedbackId));
        return FeedbackMapper.toDto(feedback);
    }

    @Override
    @Transactional
    public List<FeedbackDto> getAllFeedbacksForUser() {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return feedbackRepository.findAllByEmailOrderById(currentUser.getEmail()).stream()
                .map(FeedbackMapper::toDto)
                .collect(Collectors.toList());
    }


    //Supplementary methods
    private List<FeedbackDto> getAllFeedbacks() {
        return feedbackRepository.findAll().stream()
                .map(FeedbackMapper::toDto)
                .sorted(Comparator.comparing(FeedbackDto::getId))
                .collect(Collectors.toList());
    }

    private List<FeedbackDto> getFeedbacksByStatus(ResponseStatus status) {
        return feedbackRepository.findAllByResponseStatusOrderById(status).stream().map(FeedbackMapper::toDto).collect(Collectors.toList());
    }

    private List<FeedbackDto> getFeedbacksByMessageTopic(MessageTopic topic) {
        return feedbackRepository.findAllByMessageTopicOrderById(topic).stream()
                .map(FeedbackMapper::toDto)
                .collect(Collectors.toList());
    }

    private List<FeedbackDto> getFeedbacksByStatusAndTopic(ResponseStatus status, MessageTopic topic) {
        return feedbackRepository.findAllByResponseStatusAndMessageTopicOrderById(status, topic).stream()
                .map(FeedbackMapper::toDto)
                .collect(Collectors.toList());
    }

}

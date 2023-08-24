package team.exlab.ecohub.feedback;

import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.exlab.ecohub.exception.UserNotFoundException;
import team.exlab.ecohub.feedback.dto.FeedbackUserDto;
import team.exlab.ecohub.feedback.dto.FeedbackUserMapper;
import team.exlab.ecohub.user.model.User;
import team.exlab.ecohub.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FeedbackServiceImpl implements FeedbackService {
    private final FeedbackRepository feedbackRepository;
    private final UserRepository userRepository;
    private final FeedbackModelAssembler assembler;

    //Methods that are available for admin
    @Override
    @Transactional
    public CollectionModel<EntityModel<Feedback>> getFeedbacks(ResponseStatus status, MessageTopic topic) {
        if (!Objects.equals(status.name(), "DEFAULT") && Objects.equals(topic.name(), "DEFAULT")) {
            return getFeedbacksByStatus(status);
        } else if (Objects.equals(status.name(), "DEFAULT") && !Objects.equals(topic.name(), "DEFAULT")) {
            return getFeedbacksByMessageTopic(topic);
        } else if (!Objects.equals(status.name(), "DEFAULT") && !Objects.equals(topic.name(), "DEFAULT")) {
            return getFeedbacksByStatusAndTopic(status, topic);
        } else {
            return getAllFeedbacks();
        }
    }

    @Override
    @Transactional
    public FeedbackUserDto createFeedback(FeedbackUserDto userFeedback) {
        Feedback feedbackToSave = FeedbackUserMapper.toFeedback(userFeedback);
        feedbackToSave.setResponseStatus(ResponseStatus.OPEN);
        feedbackToSave.setMessageContent(userFeedback.getMessageContent());
        feedbackToSave.setMessageTime(LocalDateTime.now());
        return FeedbackUserMapper.toDto(feedbackRepository.save(feedbackToSave));
    }

    @Override
    @Transactional
    public Feedback createResponseToFeedback(Feedback feedback, String response) {
        Long feedback_id = feedback.getFeedbackId();
        Feedback feedbackToAlter = feedbackRepository.findById(feedback_id).orElseThrow(() -> new FeedbackNotFoundException(feedback_id));
        String feedbackResponseContent = String.join(feedbackToAlter.getResponseContent(), response);
        feedbackToAlter.setResponseStatus(ResponseStatus.IN_PROGRESS);
        feedbackToAlter.setResponseContent(feedbackResponseContent);
        feedbackToAlter.setResponseTime(LocalDateTime.now());
        return feedbackToAlter;
    }

    //Methods designated for users
    @Override
    @Transactional
    public FeedbackUserDto getOneFeedbackForUser(Long feedbackId) {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Feedback feedback = feedbackRepository.findFeedbackByUserIdAndFeedbackId(currentUser.getId(), feedbackId);
        return FeedbackUserMapper.toDto(feedback);
    }


    @Override
    @Transactional
    public List<FeedbackUserDto> getAllFeedbacksForUser() {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = userRepository.findById(currentUser.getId()).orElseThrow(() -> new UserNotFoundException("User with id -- " + currentUser.getId() + " -- not found")).getEmail();
        return feedbackRepository.findAllByEmail(email);
    }


    //Supplementary methods
    private CollectionModel<EntityModel<Feedback>> getAllFeedbacks() {
        List<EntityModel<Feedback>> models = feedbackRepository.findAll().stream().map(assembler::toModel).collect(Collectors.toList());
        return CollectionModel.of(models);
    }

    private CollectionModel<EntityModel<Feedback>> getFeedbacksByStatus(ResponseStatus status) {
        List<EntityModel<Feedback>> models = feedbackRepository.findAllByResponseStatus(status).stream().map(assembler::toModel).collect(Collectors.toList());
        return CollectionModel.of(models);
    }

    private CollectionModel<EntityModel<Feedback>> getFeedbacksByMessageTopic(MessageTopic topic) {
        List<EntityModel<Feedback>> models = feedbackRepository.findAllByMessageTopic(topic).stream().map(assembler::toModel).collect(Collectors.toList());
        return CollectionModel.of(models);
    }

    private CollectionModel<EntityModel<Feedback>> getFeedbacksByStatusAndTopic(ResponseStatus status, MessageTopic topic) {
        List<EntityModel<Feedback>> models = feedbackRepository.findAllByResponseStatusAndMessageTopic(status, topic).stream().map(assembler::toModel).collect(Collectors.toList());
        return CollectionModel.of(models);
    }

}

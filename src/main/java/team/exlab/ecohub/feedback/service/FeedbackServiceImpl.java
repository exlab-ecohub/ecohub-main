package team.exlab.ecohub.feedback.service;

import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.exlab.ecohub.exception.FeedbackNotFoundException;
import team.exlab.ecohub.feedback.assembler.FeedbackAdminDtoModelAssembler;
import team.exlab.ecohub.feedback.assembler.FeedbackUserDtoModelAssembler;
import team.exlab.ecohub.feedback.dto.FeedbackAdminDto;
import team.exlab.ecohub.feedback.dto.FeedbackAdminMapper;
import team.exlab.ecohub.feedback.dto.FeedbackUserDto;
import team.exlab.ecohub.feedback.dto.FeedbackUserMapper;
import team.exlab.ecohub.feedback.model.Feedback;
import team.exlab.ecohub.feedback.model.MessageTopic;
import team.exlab.ecohub.feedback.model.ResponseStatus;
import team.exlab.ecohub.feedback.repository.FeedbackRepository;
import team.exlab.ecohub.user.model.User;
import team.exlab.ecohub.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FeedbackServiceImpl implements FeedbackService {
    private final FeedbackRepository feedbackRepository;
    private final FeedbackAdminDtoModelAssembler adminAssembler;
    private final FeedbackUserDtoModelAssembler userAssembler;

    //Methods that are available for admin
    @Override
    @Transactional
    public CollectionModel<EntityModel<FeedbackAdminDto>> getFeedbacks(ResponseStatus status, MessageTopic topic) {
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
    public EntityModel<FeedbackAdminDto> getOneFeedback(Long feedbackId) {
        Feedback feedback = feedbackRepository.findById(feedbackId).orElseThrow(() -> new FeedbackNotFoundException(feedbackId));
        FeedbackAdminDto feedbackAdminDto = FeedbackAdminMapper.toDto(feedback);
        return adminAssembler.toModel(feedbackAdminDto);
    }

    @Override
    @Transactional
    public EntityModel<FeedbackAdminDto> createResponseToFeedback(FeedbackAdminDto feedbackAdminDto, Long feedbackId) {
        User admin = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Feedback feedbackForAnswer = feedbackRepository.findById(feedbackId).orElseThrow(() -> new FeedbackNotFoundException(feedbackId));
        feedbackForAnswer.setAdmin(admin);
        feedbackForAnswer.setResponseStatus(ResponseStatus.CLOSED);
        feedbackForAnswer.setResponseContent(feedbackAdminDto.getResponseContent());
        feedbackForAnswer.setResponseTime(LocalDateTime.now());
        return adminAssembler.toModel(FeedbackAdminMapper.toDto(feedbackForAnswer));
    }

    //Methods designated for users
    @Override
    @Transactional
    public EntityModel<FeedbackUserDto> createFeedback(FeedbackUserDto feedbackUserDto) {
        Feedback feedbackForSave = FeedbackUserMapper.toFeedback(feedbackUserDto);
        Optional<Feedback> feedback = feedbackRepository.findFirstByEmail(feedbackUserDto.getEmail());
        feedback.ifPresent(feedback1 -> {
            feedbackForSave.setUser(feedback1.getUser());
        });
        int userFeedbackCount = feedbackRepository.countFeedbackByEmail(feedbackUserDto.getEmail());
        if (userFeedbackCount == 0) {
            feedbackForSave.setUserFeedbackCount(1);
        } else {
            feedbackForSave.setUserFeedbackCount(userFeedbackCount + 1);
        }
        if (Objects.equals(feedbackUserDto.getMessageTopic(), null)) {
            feedbackForSave.setMessageTopic(MessageTopic.DEFAULT);
        }
        feedbackForSave.setMessageContent(feedbackUserDto.getMessageContent());
        feedbackForSave.setResponseStatus(ResponseStatus.OPEN);
        feedbackForSave.setMessageTime(LocalDateTime.now());
        return userAssembler.toModel(FeedbackUserMapper.toDto(feedbackRepository.save(feedbackForSave)));
    }

    @Override
    @Transactional
    public EntityModel<FeedbackUserDto> getOneFeedbackForUser(Integer userFeedbackCount) {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Feedback feedback = feedbackRepository.findFeedbackByUserFeedbackCountAndUser(userFeedbackCount, currentUser);
        return userAssembler.toModel(FeedbackUserMapper.toDto(feedback));
    }


    @Override
    @Transactional
    public CollectionModel<EntityModel<FeedbackUserDto>> getAllFeedbacksForUser() {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<EntityModel<FeedbackUserDto>> feedbackModels = feedbackRepository.findAllByEmail(currentUser.getEmail()).stream().map(FeedbackUserMapper::toDto).map(userAssembler::toModel).collect(Collectors.toList());
        return CollectionModel.of(feedbackModels);
    }


    //Supplementary methods
    private CollectionModel<EntityModel<FeedbackAdminDto>> getAllFeedbacks() {
        List<EntityModel<FeedbackAdminDto>> models = feedbackRepository.findAll().stream().map(FeedbackAdminMapper::toDto).map(adminAssembler::toModel).collect(Collectors.toList());
        return CollectionModel.of(models);
    }

    private CollectionModel<EntityModel<FeedbackAdminDto>> getFeedbacksByStatus(ResponseStatus status) {
        List<EntityModel<FeedbackAdminDto>> models = feedbackRepository.findAllByResponseStatus(status).stream().map(FeedbackAdminMapper::toDto).map(adminAssembler::toModel).collect(Collectors.toList());
        return CollectionModel.of(models);
    }

    private CollectionModel<EntityModel<FeedbackAdminDto>> getFeedbacksByMessageTopic(MessageTopic topic) {
        List<EntityModel<FeedbackAdminDto>> models = feedbackRepository.findAllByMessageTopic(topic).stream().map(FeedbackAdminMapper::toDto).map(adminAssembler::toModel).collect(Collectors.toList());
        return CollectionModel.of(models);
    }

    private CollectionModel<EntityModel<FeedbackAdminDto>> getFeedbacksByStatusAndTopic(ResponseStatus status, MessageTopic topic) {
        List<EntityModel<FeedbackAdminDto>> models = feedbackRepository.findAllByResponseStatusAndMessageTopic(status, topic).stream().map(FeedbackAdminMapper::toDto).map(adminAssembler::toModel).collect(Collectors.toList());
        return CollectionModel.of(models);
    }

}

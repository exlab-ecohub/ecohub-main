package team.exlab.ecohub.feedback.controller;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.*;
import team.exlab.ecohub.feedback.*;
import team.exlab.ecohub.feedback.dto.FeedbackAdminDto;
import team.exlab.ecohub.feedback.dto.FeedbackAdminMapper;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/test")
public class AdminFeedbackController {
    private final FeedbackRepository feedbackRepository;
    private final FeedbackServiceImpl service;
    private final FeedbackModelAssembler assembler;

    public AdminFeedbackController(FeedbackRepository feedbackRepository, FeedbackServiceImpl service, FeedbackModelAssembler assembler) {
        this.feedbackRepository = feedbackRepository;
        this.service = service;
        this.assembler = assembler;
    }

    @GetMapping("/messages")
    public CollectionModel<EntityModel<Feedback>> getAllMessages() {
        List<EntityModel<Feedback>> models = feedbackRepository.findAll().stream().map(assembler::toModel).collect(Collectors.toList());
        return CollectionModel.of(models);
    }

    @GetMapping("/messages/{id}")
    public EntityModel<Feedback> getFeedbackById(@PathVariable Long id) {
        Feedback feedback = feedbackRepository.findById(id).orElseThrow(() -> new FeedbackNotFoundException(id));
        return assembler.toModel(feedback);
    }

    @PostMapping("/messages/{id}")
    public FeedbackAdminDto makeResponse(@RequestBody FeedbackAdminDto feedbackAdminDto, @PathVariable Long id) {
        Feedback feedback = feedbackRepository.findById(id).orElseThrow(() -> new FeedbackNotFoundException(id));
        String response = feedbackAdminDto.getResponseContent();
        service.createResponseToFeedback(feedback, response);
        return FeedbackAdminMapper.toDto(feedbackRepository.save(feedback));
    }

}

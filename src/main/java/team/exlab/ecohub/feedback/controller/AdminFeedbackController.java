package team.exlab.ecohub.feedback.controller;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.*;
import team.exlab.ecohub.feedback.*;
import team.exlab.ecohub.feedback.ResponseStatus;

@RestController
@RequestMapping("/test")
public class AdminFeedbackController {
    private final FeedbackRepository repository;
    private final FeedbackServiceImpl service;
    private final FeedbackModelAssembler assembler;

    public AdminFeedbackController(FeedbackRepository feedbackRepository, FeedbackServiceImpl service, FeedbackModelAssembler assembler) {
        this.repository = feedbackRepository;
        this.service = service;
        this.assembler = assembler;
    }

    @GetMapping("/messages")
    public CollectionModel<EntityModel<Feedback>> getAllMessages(@RequestParam(required = false, defaultValue = "DEFAULT") String status,
                                                                 @RequestParam(required = false, defaultValue = "DEFAULT") String topic) {
        return service.getFeedbacks(ResponseStatus.valueOf(status), MessageTopic.valueOf(topic));
    }

    @GetMapping("/messages/{id}")
    public EntityModel<Feedback> getFeedbackById(@PathVariable Long id) {
        Feedback feedback = repository.findById(id).orElseThrow(() -> new FeedbackNotFoundException(id));
        return assembler.toModel(feedback);
    }

    @PostMapping("/messages/{id}")
    public Feedback makeResponse(@RequestBody Feedback feedbackAdminDto, @PathVariable Long id) {
        Feedback feedback = repository.findById(id).orElseThrow(() -> new FeedbackNotFoundException(id));
        String response = feedbackAdminDto.getResponseContent();
        service.createResponseToFeedback(feedback, response);
        return repository.save(feedback);
    }

}

package team.exlab.ecohub.feedback.controller;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.*;
import team.exlab.ecohub.feedback.Feedback;
import team.exlab.ecohub.feedback.FeedbackModelAssembler;
import team.exlab.ecohub.feedback.FeedbackNotFoundException;
import team.exlab.ecohub.feedback.FeedbackRepository;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class AdminFeedbackController {
    private final FeedbackRepository feedbackRepository;
    private final FeedbackModelAssembler assembler;

    public AdminFeedbackController(FeedbackRepository feedbackRepository, FeedbackModelAssembler assembler) {
        this.feedbackRepository = feedbackRepository;
        this.assembler = assembler;
    }

    @GetMapping("/admin/messages")
    public CollectionModel<EntityModel<Feedback>> getAllMessages() {
        List<EntityModel<Feedback>> models = feedbackRepository.findAll().stream().
                map(assembler::toModel).collect(Collectors.toList());
        return CollectionModel.of(models);
    }

    @GetMapping("/admin/messages/{id}")
    public EntityModel<Feedback> getFeedbackById(@PathVariable Long id) {
        Feedback feedback = feedbackRepository.findById(id).orElseThrow(() -> new FeedbackNotFoundException(id));
        return assembler.toModel(feedback);
    }

    @PostMapping("/admin/messages/{id}")
    public Feedback makeResponse(@RequestBody Feedback feedbackToSend, @PathVariable Long id) {
        return feedbackRepository.save(feedbackToSend);
    }

}

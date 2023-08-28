package team.exlab.ecohub.feedback.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.*;
import team.exlab.ecohub.feedback.dto.FeedbackUserDto;
import team.exlab.ecohub.feedback.service.FeedbackService;

@RestController
@RequiredArgsConstructor
public class UserFeedbackController {
    private final FeedbackService service;


    @PostMapping("/feedbacks")
    public EntityModel<FeedbackUserDto> createFeedback(@RequestBody FeedbackUserDto userFeedback) {
        return service.createFeedback(userFeedback);
    }
    @GetMapping("/user/feedbacks")
    public CollectionModel<EntityModel<FeedbackUserDto>> getAllFeedbacks() {
        return service.getAllFeedbacksForUser();
    }

    @GetMapping("/user/feedbacks/{userFeedbackCount}")
    public EntityModel<FeedbackUserDto> getOneFeedback(@PathVariable Integer userFeedbackCount) {
        return service.getOneFeedbackForUser(userFeedbackCount);
    }
}

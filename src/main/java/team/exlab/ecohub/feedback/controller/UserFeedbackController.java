package team.exlab.ecohub.feedback.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import team.exlab.ecohub.feedback.dto.FeedbackDto;
import team.exlab.ecohub.feedback.service.FeedbackService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class UserFeedbackController {
    private final FeedbackService service;


    @PostMapping("/feedbacks")
    public void createFeedback(@Valid @RequestBody FeedbackDto userFeedback) {
        service.createFeedback(userFeedback);
    }

    @GetMapping("/user/feedbacks")
    public List<FeedbackDto> getAllFeedbacks() {
        return service.getAllFeedbacksForUser();
    }

    @GetMapping("/user/feedbacks/{id}")
    public FeedbackDto getFeedback(@PathVariable Long id) {
        return service.getFeedbackForUser(id);
    }
}

package team.exlab.ecohub.feedback.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import team.exlab.ecohub.feedback.*;
import team.exlab.ecohub.feedback.dto.FeedbackUserDto;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class UserFeedbackController {
    private final FeedbackService service;


    @PostMapping("/feedbacks")
    public FeedbackUserDto createFeedback(@RequestBody FeedbackUserDto userFeedback) {
        return service.createFeedback(userFeedback);
    }
    @GetMapping("/user/feedbacks")
    public List<FeedbackUserDto> getAllFeedbacks() {
        return service.getAllFeedbacksForUser();
    }

    @GetMapping("/user/feedbacks/{feedbackId}")
    public FeedbackUserDto getOneFeedback(@PathVariable Long feedbackId) {
        return service.getOneFeedbackForUser(feedbackId);
    }
}

package team.exlab.ecohub.feedback.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import team.exlab.ecohub.feedback.*;
import team.exlab.ecohub.feedback.dto.FeedbackUserDto;
import team.exlab.ecohub.feedback.dto.FeedbackUserMapper;

@RestController
@RequiredArgsConstructor
public class UserFeedbackController {
    private final FeedbackService feedbackService;

    @PostMapping("/feedback")
    public FeedbackUserDto createFeedback(@RequestBody Feedback feedback) {
        FeedbackUserDto feedbackUserDto = FeedbackUserMapper.toDto(feedback);
        feedbackService.createFeedback(feedbackUserDto);
        return feedbackUserDto;
    }
}

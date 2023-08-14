package team.exlab.ecohub.feedback.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import team.exlab.ecohub.feedback.*;
import team.exlab.ecohub.feedback.dto.FeedbackUserDto;
import team.exlab.ecohub.feedback.dto.FeedbackUserMapper;

import java.util.List;

@RestController
@RequestMapping("/feedbacks")
@RequiredArgsConstructor
public class UserFeedbackController {
    private final FeedbackService feedbackService;
    private final FeedbackRepository repository;
    private final FeedbackRepository1 repository1;
    @GetMapping
    public List<Feedback1> getFeedbacks() {
        List<Feedback1> results = repository1.findAll();
        return results;
    }
    @PostMapping
    public FeedbackUserDto createFeedback(@RequestBody FeedbackUserDto userFeedback) {
        return feedbackService.createFeedback(userFeedback);
    }
    @GetMapping("/{id}")
    public FeedbackUserDto readFeedback(@PathVariable Long id) {
        Feedback feedback = repository.findById(id).orElseThrow(() -> new FeedbackNotFoundException(id));
        return FeedbackUserMapper.toDto(feedback);
    }
}

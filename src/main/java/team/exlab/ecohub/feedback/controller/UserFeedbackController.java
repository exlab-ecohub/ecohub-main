package team.exlab.ecohub.feedback.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import team.exlab.ecohub.feedback.*;
import team.exlab.ecohub.feedback.dto.FeedbackUserDto;
import team.exlab.ecohub.feedback.dto.FeedbackUserMapper;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class UserFeedbackController {
    private final FeedbackService feedbackService;
    private final FeedbackRepository repository;


    @PostMapping("/feedbacks")
    public FeedbackUserDto createFeedback(@RequestBody FeedbackUserDto userFeedback) {
        return feedbackService.createFeedback(userFeedback);
    }
    @GetMapping("/user/feedbacks")
    public List<FeedbackUserDto> getAllFeedbacks(@RequestParam Long userId) {
        //User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return feedbackService.showFeedbacksForUser(userId);
    }

    @GetMapping("/user/feedbacks/{id}")
    public FeedbackUserDto readFeedback(@PathVariable Long id) {
        Feedback feedback = repository.findById(id).orElseThrow(() -> new FeedbackNotFoundException(id));
        return FeedbackUserMapper.toDto(feedback);
    }
}

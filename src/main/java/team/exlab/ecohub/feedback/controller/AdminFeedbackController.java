package team.exlab.ecohub.feedback.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import team.exlab.ecohub.feedback.dto.FeedbackDto;
import team.exlab.ecohub.feedback.model.MessageTopic;
import team.exlab.ecohub.feedback.model.ResponseStatus;
import team.exlab.ecohub.feedback.service.FeedbackServiceImpl;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminFeedbackController {
    private final FeedbackServiceImpl service;

    @GetMapping("/feedbacks")
    public List<FeedbackDto> getAllMessages(@RequestParam(required = false, defaultValue = "DEFAULT") String status,
                                            @RequestParam(required = false, defaultValue = "DEFAULT") String topic) {
        return service.getFeedbacks(ResponseStatus.valueOf(status), MessageTopic.valueOf(topic));
    }

    @GetMapping("/feedbacks/{id}")
    public FeedbackDto getFeedbackById(@PathVariable Long id) {
        return service.getFeedback(id);
    }

    @PostMapping("/feedbacks/{id}")
    public FeedbackDto makeResponse(@RequestBody FeedbackDto feedbackDto, @PathVariable Long id) {
        return service.createResponseToFeedback(feedbackDto, id);
    }

}

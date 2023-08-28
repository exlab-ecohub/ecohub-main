package team.exlab.ecohub.feedback.controller;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.*;
import team.exlab.ecohub.feedback.dto.FeedbackAdminDto;
import team.exlab.ecohub.feedback.model.MessageTopic;
import team.exlab.ecohub.feedback.model.ResponseStatus;
import team.exlab.ecohub.feedback.service.FeedbackServiceImpl;

@RestController
@RequestMapping("/admin")
public class AdminFeedbackController {
    private final FeedbackServiceImpl service;

    public AdminFeedbackController(FeedbackServiceImpl service) {
        this.service = service;
    }

    @GetMapping("/messages")
    public CollectionModel<EntityModel<FeedbackAdminDto>> getAllMessages(@RequestParam(required = false, defaultValue = "DEFAULT") String status,
                                                                         @RequestParam(required = false, defaultValue = "DEFAULT") String topic) {
        return service.getFeedbacks(ResponseStatus.valueOf(status), MessageTopic.valueOf(topic));
    }

    @GetMapping("/messages/{id}")
    public EntityModel<FeedbackAdminDto> getFeedbackById(@PathVariable Long id) {
        return service.getOneFeedback(id);
    }

    @PostMapping("/messages/{id}")
    public EntityModel<FeedbackAdminDto> makeResponse(@RequestBody FeedbackAdminDto feedbackAdminDto, @PathVariable Long id) {
        return service.createResponseToFeedback(feedbackAdminDto, id);
    }

}

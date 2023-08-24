package team.exlab.ecohub.feedback;

import team.exlab.ecohub.feedback.controller.AdminFeedbackController;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import team.exlab.ecohub.feedback.controller.UserFeedbackController;
import team.exlab.ecohub.feedback.dto.FeedbackUserDto;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class FeedbackModelAssembler implements RepresentationModelAssembler<Feedback, EntityModel<Feedback>> {
    @Override
    public EntityModel<Feedback> toModel(Feedback entity) {
        return EntityModel.of(entity,
                linkTo(methodOn(AdminFeedbackController.class).getFeedbackById(entity.getFeedbackId())).withSelfRel(),
                linkTo(methodOn(AdminFeedbackController.class).getAllMessages(entity.getResponseStatus().name(),entity.getMessageTopic().name())).withRel("messages"));
    }

    /*public EntityModel<FeedbackUserDto> toModel(FeedbackUserDto entity) {
        EntityModel.of(entity,
                linkTo(methodOn(UserFeedbackController.class).readFeedback(entity.getEmail())).withSelfRel(),
                )
        return null;
    }*/
}


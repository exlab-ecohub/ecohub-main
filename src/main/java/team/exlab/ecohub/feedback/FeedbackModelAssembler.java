package team.exlab.ecohub.feedback;

import team.exlab.ecohub.feedback.controller.AdminFeedbackController;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class FeedbackModelAssembler implements RepresentationModelAssembler<Feedback, EntityModel<Feedback>> {
    @Override
    public EntityModel<Feedback> toModel(Feedback entity) {
        return EntityModel.of(entity,
                linkTo(methodOn(AdminFeedbackController.class).getFeedbackById(entity.getFeedback_id())).withSelfRel(),
                linkTo(methodOn(AdminFeedbackController.class).getAllMessages()).withRel("messages"));
    }
}


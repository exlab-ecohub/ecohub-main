package team.exlab.ecohub.feedback.assembler;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import team.exlab.ecohub.feedback.controller.UserFeedbackController;
import team.exlab.ecohub.feedback.dto.FeedbackUserDto;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class FeedbackUserDtoModelAssembler implements RepresentationModelAssembler<FeedbackUserDto, EntityModel<FeedbackUserDto>> {
    @Override
    public EntityModel<FeedbackUserDto> toModel(FeedbackUserDto entity) {

        return EntityModel.of(entity,
                linkTo(methodOn(UserFeedbackController.class).getOneFeedback(entity.getUserFeedbackCount())).withSelfRel(),
                linkTo(methodOn(UserFeedbackController.class).getAllFeedbacks()).withRel("feedbacks"));
    }
}

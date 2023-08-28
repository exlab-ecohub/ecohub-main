package team.exlab.ecohub.feedback.assembler;

import team.exlab.ecohub.feedback.controller.AdminFeedbackController;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import team.exlab.ecohub.feedback.dto.FeedbackAdminDto;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class FeedbackAdminDtoModelAssembler implements RepresentationModelAssembler<FeedbackAdminDto, EntityModel<FeedbackAdminDto>> {
    @Override
    public EntityModel<FeedbackAdminDto> toModel(FeedbackAdminDto entity) {
        return EntityModel.of(entity,
                linkTo(methodOn(AdminFeedbackController.class).getFeedbackById(entity.getId())).withSelfRel(),
                linkTo(methodOn(AdminFeedbackController.class).getAllMessages(entity.getResponseStatus().name(),entity.getMessageTopic().name())).withRel("messages"));
    }

    /*public EntityModel<FeedbackUserDto> toModel(FeedbackUserDto entity) {
        EntityModel.of(entity,
                linkTo(methodOn(UserFeedbackController.class).readFeedback(entity.getEmail())).withSelfRel(),
                )
        return null;
    }*/
}


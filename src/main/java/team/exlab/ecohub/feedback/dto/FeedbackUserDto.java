package team.exlab.ecohub.feedback.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import team.exlab.ecohub.feedback.model.MessageTopic;
import team.exlab.ecohub.feedback.model.ResponseStatus;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class FeedbackUserDto {
    @NotBlank(message = "name is mandatory and can not be empty!")
    @Size(max = 30, message = "size should be less than 30")
    private String name;
    private int userFeedbackCount;
    @NotBlank(message = "email is mandatory and can not be empty!")
    @Size(min = 5, max = 50, message = "size should be between 5 an 50 characters")
    @Email(message = "provide a valid email", regexp = "[\\w\\.\\-]+@[a-z\\.]+\\.(ru|com|net|org)")
    private String email;
    private MessageTopic messageTopic;
    @Size(max = 500, message = "message should not be greater than 500 characters")
    private String messageContent;
    private LocalDateTime messageTime;
    private String responseContent;
    private LocalDateTime responseTime;
    private ResponseStatus responseStatus;
}

package team.exlab.ecohub.feedback.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import team.exlab.ecohub.feedback.model.MessageTopic;
import team.exlab.ecohub.feedback.model.ResponseStatus;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class FeedbackDto {
    private Long id;
    @NotBlank(message = "name is mandatory and can not be empty!")
    @Size(max = 30, message = "size should be less than 30")
    @Pattern(regexp = "[0-9а-яёА-ЯЁa-zA-Z-_ ]{2,30}",
            message = "name must meet requirements")
    private String name;
    @NotBlank(message = "email is mandatory and can not be empty!")
    @Size(min = 5, max = 50, message = "size should be between 5 an 50 characters")
    @Email(message = "provide a valid email", regexp = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$")
    private String email;
    private MessageTopic messageTopic;
    @NotBlank(message = "message is mandatory and can not be empty!")
    @Size(max = 500, message = "message should not be greater than 500 characters")
    @Pattern(regexp = "[0-9а-яёА-ЯЁa-zA-Z<>{}\\[\\]()?!,.:;'/|\"@№#$%^&*\\-_ +=`~]{1,500}",
            message = "message content must meet requirements")
    private String messageContent;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME, pattern = "yyyy-MM-dd HH:mm")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime messageTime;
    private String responseContent;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime responseTime;
    private ResponseStatus responseStatus;
}

package team.exlab.ecohub.user.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@Setter
public class PasswordChangeDto {
    @NotBlank(message = "password is mandatory and can not be empty!")
    @Size(min = 8, max = 20, message = "size should be less than 30")
    @Pattern(regexp = "(?=.*[0-9])(?=.*[<>{}\\[\\]()?!,.:;'/|\"@№#$%^&*\\-_ +=`~])(?=.*[a-z])(?=.*[A-Z])" +
            "[0-9a-zA-Z<>{}\\[\\]()?!,.:;'/|\"@№#$%^&*\\-_ +=`~]{8,20}",
            message = "password must meet security requirements")
    private String newPassword;
}

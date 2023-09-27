package team.exlab.ecohub.auth.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@Setter
public class SignupAdminRequestDto {
    @NotBlank(message = "username is mandatory and can not be empty!")
    @Size(min = 6, max = 15)
    @Pattern(regexp = "[0-9a-zA-Z-_]{6,15}",
            message = "username must meet security requirements")
    private String username;
    @NotBlank(message = "password is mandatory and can not be empty!")
    @Size(min = 8, max = 20)
    @Pattern(regexp = "(?=.*[0-9])(?=.*[<>{}\\[\\]()?!,.:;'/|\"@№#$%^&*\\-_ +=`~])(?=.*[a-z])(?=.*[A-Z])" +
            "[0-9a-zA-Z<>{}\\[\\]()?!,.:;'/|\"@№#$%^&*\\-_ +=`~]{8,20}",
            message = "password must meet security requirements")
    private String password;
}

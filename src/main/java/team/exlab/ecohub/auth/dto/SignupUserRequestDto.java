package team.exlab.ecohub.auth.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@Setter
public class SignupUserRequestDto {
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
    @NotBlank(message = "email is mandatory and can not be empty!")
    @Size(min = 5, max = 50, message = "size should be between 5 an 50 characters")
    @Email(message = "provide a valid email", regexp = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$")
    private String email;
}

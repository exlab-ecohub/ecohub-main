package team.exlab.ecohub.auth.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
public class SignupUserRequestDto {
    @NotNull
    @Size(min = 5, max = 100)
    private String username;
    @NotNull
    @Size(min = 10, max = 100)
    private String password;
    @NotNull
    @Email
    @Size(min = 5, max = 100)
    private String email;
}

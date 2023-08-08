package team.exlab.ecohub.auth.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
public class LoginRequestDto {
    @NotNull
    @Size(min = 5, max = 100)
    private String usernameOrEmail;
    @NotNull
    @Size(min = 10, max = 100)
    private String password;
    private boolean rememberMe;
}
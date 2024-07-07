package team.exlab.ecohub.auth.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequestDto {
    @NotNull
    @Size(min = 6, max = 15)
    private String usernameOrEmail;
    @NotNull
    @Size(min = 8, max = 20)
    private String password;
    private boolean rememberMe;
}
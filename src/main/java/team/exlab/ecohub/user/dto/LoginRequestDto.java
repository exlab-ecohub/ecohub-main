package team.exlab.ecohub.user.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Size;

@Getter
@Setter
public class LoginRequestDto {
    @Size(min = 5, max = 100)
    private String username;
    @Size(min = 10, max = 100)
    private String password;
    private boolean isRememberMe;
}
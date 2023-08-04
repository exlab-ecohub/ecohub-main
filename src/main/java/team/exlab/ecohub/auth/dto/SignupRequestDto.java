package team.exlab.ecohub.auth.dto;

import lombok.Getter;
import lombok.Setter;
import team.exlab.ecohub.user.model.ERole;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

@Getter
@Setter
public class SignupRequestDto {
    @Size(min = 5, max = 100)
    private String username;
    @Size(min = 10, max = 100)
    private String password;
    private ERole role;
    @Email
    @Size(min = 5, max = 100)
    private String email;
}

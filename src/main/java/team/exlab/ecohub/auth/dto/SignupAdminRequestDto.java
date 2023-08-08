package team.exlab.ecohub.auth.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
public class SignupAdminRequestDto {
    @NotNull
    @Size(min = 5, max = 100)
    private String username;
    @NotNull
    @Size(min = 10, max = 100)
    private String password;
}

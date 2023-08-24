package team.exlab.ecohub.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor()
public class JwtResponseDto {
    private String accessToken;
    private String refreshToken;
    private String username;
    private String email;
}

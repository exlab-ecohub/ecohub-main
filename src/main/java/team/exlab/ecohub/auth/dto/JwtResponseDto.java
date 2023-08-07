package team.exlab.ecohub.auth.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor()
public class JwtResponseDto {
    public static final String TYPE = "Bearer";
    @JsonProperty("access_token")
    private String accessToken;
    @JsonProperty("refresh_token")
    private String refreshToken;
    @JsonIgnore
    private Long id;
    private String username;
    private String email;
    private List<String> roles;
}

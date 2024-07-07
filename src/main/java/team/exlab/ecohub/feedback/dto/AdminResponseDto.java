package team.exlab.ecohub.feedback.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminResponseDto {
    @NotBlank(message = "response is mandatory and can not be empty!")
    private String responseContent;
}

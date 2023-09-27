package team.exlab.ecohub.feedback.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class AdminResponseDto {
    @NotBlank(message = "response is mandatory and can not be empty!")
    private String responseContent;
}

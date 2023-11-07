package team.exlab.ecohub.feedback.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class AdminResponseDto {
    private Long id;
    @NotBlank(message = "response is mandatory and can not be empty!")
    private String responseContent;
}

package team.exlab.ecohub.recyclingpoint.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class RecyclingPointPartInfoDto {
    private Long id;
    private String name;
    private String address;
    private String phoneNumber;
    private String website;
}

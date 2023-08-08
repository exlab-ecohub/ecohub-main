package team.exlab.ecohub.recyclingpoint;

import team.exlab.ecohub.recyclingpoint.dto.RecyclingPointDto;
import team.exlab.ecohub.recyclingpoint.dto.RecyclingPointPartInfoDto;
import team.exlab.ecohub.recyclingpoint.model.RecyclableType;
import team.exlab.ecohub.recyclingpoint.model.RecyclingPoint;

import java.util.Set;
import java.util.stream.Collectors;

public class RecyclingPointMapper {
    private RecyclingPointMapper() {
    }

    public static RecyclingPointPartInfoDto toPartInfoDto(RecyclingPoint recyclingPoint) {
        return RecyclingPointPartInfoDto.builder()
                .id(recyclingPoint.getId())
                .name(recyclingPoint.getName())
                .address(recyclingPoint.getAddress())
                .phoneNumber(recyclingPoint.getPhoneNumber())
                .website(recyclingPoint.getWebsite())
                .displayed(recyclingPoint.isDisplayed()).build();
    }

    public static RecyclingPointDto toDto(RecyclingPoint recyclingPoint) {
        return RecyclingPointDto.builder()
                .id(recyclingPoint.getId())
                .name(recyclingPoint.getName())
                .address(recyclingPoint.getAddress())
                .phoneNumber(recyclingPoint.getPhoneNumber())
                .website(recyclingPoint.getWebsite())
                .location(recyclingPoint.getLocation())
                .workingHours(recyclingPoint.getWorkingHours())
                .recyclableTypes(recyclingPoint.getRecyclableTypes().stream()
                        .map(type -> type.getName().name())
                        .collect(Collectors.toSet()))
                .displayed(recyclingPoint.isDisplayed()).build();
    }

    public static RecyclingPoint toPoint(RecyclingPointDto recyclingPointDto,
                                         Set<RecyclableType> recyclableTypes) {
        return RecyclingPoint.builder()
                .id(recyclingPointDto.getId())
                .name(recyclingPointDto.getName())
                .address(recyclingPointDto.getAddress())
                .phoneNumber(recyclingPointDto.getPhoneNumber())
                .website(recyclingPointDto.getWebsite())
                .location(recyclingPointDto.getLocation())
                .workingHours(recyclingPointDto.getWorkingHours())
                .recyclableTypes(recyclableTypes)
                .displayed(recyclingPointDto.isDisplayed()).build();
    }
}

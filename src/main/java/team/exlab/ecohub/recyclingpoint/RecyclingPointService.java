package team.exlab.ecohub.recyclingpoint;

import team.exlab.ecohub.recyclingpoint.dto.RecyclingPointDto;

import java.util.List;
import java.util.Set;

public interface RecyclingPointService {
    List<RecyclingPointDto> getPointsDto(Set<String> types,
                                         String displayed,
                                         Integer from,
                                         Integer size);

    RecyclingPointDto getPoint(Long pointId);

    RecyclingPointDto createPoint(RecyclingPointDto point);

    RecyclingPointDto updatePoint(Long pointId, RecyclingPointDto updatedPoint);

    void deletePoint(Long pointId);
}

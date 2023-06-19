package team.exlab.ecohub.recyclingpoint;

import team.exlab.ecohub.recyclingpoint.dto.RecyclingPointDto;
import team.exlab.ecohub.recyclingpoint.dto.RecyclingPointPartInfoDto;

import java.util.List;
import java.util.Set;

public interface RecyclingPointService {
    List<RecyclingPointPartInfoDto> getPoints(Set<String> types,
                                              Integer from,
                                              Integer size);

    RecyclingPointDto getPoint(Long pointId);

    RecyclingPointDto createPoint(RecyclingPointDto point);

    RecyclingPointDto updatePoint(Long pointId, RecyclingPointDto updatedPoint);

    void deletePoint(Long pointId);
}

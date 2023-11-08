package team.exlab.ecohub.recyclingpoint;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.exlab.ecohub.exception.RecyclingPointNotFoundException;
import team.exlab.ecohub.pageable.OffsetLimitPageable;
import team.exlab.ecohub.recyclingpoint.dto.RecyclingPointDto;
import team.exlab.ecohub.recyclingpoint.dto.RecyclingPointPartInfoDto;
import team.exlab.ecohub.recyclingpoint.model.ERecyclableType;
import team.exlab.ecohub.recyclingpoint.model.RecyclableType;
import team.exlab.ecohub.recyclingpoint.model.RecyclingPoint;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class RecyclingPointServiceImpl implements RecyclingPointService {
    private final RecyclingPointRepository pointRepository;
    private final RecyclableTypeRepository typeRepository;

    @Override
    @Transactional
    public List<RecyclingPointPartInfoDto> getPoints(Set<String> types, String displayed, Integer from, Integer size) {
        List<RecyclingPoint> recyclingPoints;
        size = size == 0 ? (int) pointRepository.count() - from : size;
        if (types == null || types.isEmpty()) {
            if (displayed.equals("null")) {
                recyclingPoints = pointRepository.findAllByOrderById(OffsetLimitPageable.of(from, size));
            } else {
                recyclingPoints = pointRepository.findAllByDisplayedOrderById(Boolean.parseBoolean(displayed), OffsetLimitPageable.of(from, size));
            }
        } else {
            Set<RecyclableType> recyclableTypes = new HashSet<>();
            types.forEach(type -> recyclableTypes.add(typeRepository.findByName(ERecyclableType.valueOf(type))));
            if (displayed.equals("null")) {
                recyclingPoints = pointRepository.findAllDistinctByRecyclableTypesInOrderById(recyclableTypes,
                        OffsetLimitPageable.of(from, size));
            } else {
                recyclingPoints = pointRepository.findAllDistinctByDisplayedAndRecyclableTypesInOrderById(Boolean.parseBoolean(displayed),
                        recyclableTypes,
                        OffsetLimitPageable.of(from, size));
            }
        }
        return recyclingPoints.stream()
                .map(RecyclingPointMapper::toPartInfoDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public RecyclingPointDto getPoint(Long pointId) {
        return RecyclingPointMapper.toDto(pointRepository.findById(pointId)
                .orElseThrow(() -> new RecyclingPointNotFoundException(pointId)));
    }

    @Override
    @Transactional
    public RecyclingPointDto createPoint(RecyclingPointDto point) {
        Set<RecyclableType> types = new HashSet<>();
        point.getRecyclableTypes().forEach(type -> types.add(typeRepository.findByName(ERecyclableType.valueOf(type))));
        RecyclingPoint recyclingPoint = RecyclingPointMapper.toPoint(point, types);
        return RecyclingPointMapper.toDto(pointRepository.save(recyclingPoint));
    }

    @Override
    @Transactional
    public RecyclingPointDto updatePoint(Long pointId, RecyclingPointDto updatedPoint) {
        RecyclingPoint recyclingPoint = pointRepository.findById(pointId)
                .orElseThrow(() -> new RecyclingPointNotFoundException(pointId));
        recyclingPoint.setName(updatedPoint.getName());
        recyclingPoint.setAddress(updatedPoint.getAddress());
        recyclingPoint.setPhoneNumber(updatedPoint.getPhoneNumber());
        recyclingPoint.setWebsite(updatedPoint.getWebsite());
        recyclingPoint.setLocation(updatedPoint.getLocation());
        recyclingPoint.setWorkingHours(RecyclingPointMapper.workingHoursToMap(updatedPoint.getWorkingHours()));
        recyclingPoint.setRecyclableTypes(
                updatedPoint.getRecyclableTypes().stream()
                        .map(type -> typeRepository.findByName(ERecyclableType.valueOf(type)))
                        .collect(Collectors.toSet())
        );
        recyclingPoint.setDisplayed(updatedPoint.isDisplayed());
        return RecyclingPointMapper.toDto(pointRepository.save(recyclingPoint));
    }

    @Override
    @Transactional
    public void deletePoint(Long pointId) {
        if (!pointRepository.existsById(pointId)) {
            throw new RecyclingPointNotFoundException(pointId);
        }
        pointRepository.deleteById(pointId);
    }
}

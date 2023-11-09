package team.exlab.ecohub.recyclingpoint;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.exlab.ecohub.exception.RecyclingPointNotFoundException;
import team.exlab.ecohub.pageable.OffsetLimitPageable;
import team.exlab.ecohub.recyclingpoint.dto.RecyclingPointDto;
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
    private static final String WRONG_RECYCLABLE_TYPES = "Some of above recyclableTypes values are wrong=%s";

    @Override
    @Transactional
    public List<RecyclingPointDto> getPointsDto(Set<String> types, String displayed, Integer from, Integer size) {
        List<RecyclingPoint> recyclingPoints = getRawPoints(types, displayed, from, size);
        return recyclingPoints.stream()
                .map(RecyclingPointMapper::toDto)
                .collect(Collectors.toList());
    }

    private List<RecyclingPoint> getRawPoints(Set<String> types, String displayed, Integer from, Integer size) {
        List<RecyclingPoint> recyclingPoints;
        from = from >= 1 ? from - 1 : from;
        size = size == 0 ? (int) pointRepository.count() - from : size;
        if (types == null || types.isEmpty()) {
            if (displayed.equals("null")) {
                recyclingPoints = pointRepository.findAllByOrderById(OffsetLimitPageable.of(from, size));
            } else {
                recyclingPoints = pointRepository.findAllByDisplayedOrderById(Boolean.parseBoolean(displayed), OffsetLimitPageable.of(from, size));
            }
        } else {
            Set<RecyclableType> recyclableTypes = new HashSet<>();
            types.forEach(type ->
                    recyclableTypes.add(typeRepository.findByRusName(type).orElseThrow(() ->
                            new IllegalArgumentException(
                                    String.format(WRONG_RECYCLABLE_TYPES, types)))));
            if (displayed.equals("null")) {
                recyclingPoints = pointRepository.findAllDistinctByRecyclableTypesInOrderById(recyclableTypes,
                        OffsetLimitPageable.of(from, size));
            } else {
                recyclingPoints = pointRepository.findAllDistinctByDisplayedAndRecyclableTypesInOrderById(Boolean.parseBoolean(displayed),
                        recyclableTypes,
                        OffsetLimitPageable.of(from, size));
            }
        }
        return recyclingPoints;
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
        point.getRecyclableTypes().forEach(type ->
                types.add(typeRepository.findByRusName(type).orElseThrow(() ->
                        new IllegalArgumentException(
                                String.format(WRONG_RECYCLABLE_TYPES, point.getRecyclableTypes())))));
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
                        .map(type -> typeRepository.findByRusName(type).orElseThrow(() ->
                                new IllegalArgumentException(
                                        String.format(WRONG_RECYCLABLE_TYPES, updatedPoint.getRecyclableTypes()))))
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

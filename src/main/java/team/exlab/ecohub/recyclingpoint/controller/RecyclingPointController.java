package team.exlab.ecohub.recyclingpoint.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import team.exlab.ecohub.recyclingpoint.RecyclingPointService;
import team.exlab.ecohub.recyclingpoint.dto.RecyclingPointDto;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/recycling-points")
@RequiredArgsConstructor
public class RecyclingPointController {
    private final RecyclingPointService recyclingPointService;

    @GetMapping
    public List<RecyclingPointDto> getPoints(@RequestParam(required = false) Set<String> types,
                                                     @RequestParam(required = false, defaultValue = "null") String displayed,
                                                     @RequestParam(required = false, defaultValue = "0") Integer from,
                                                     @RequestParam(required = false, defaultValue = "0") Integer size) {
        return recyclingPointService.getPointsDto(types, displayed, from, size);
    }

    @GetMapping("/{pointId}")
    public RecyclingPointDto getPoint(@PathVariable Long pointId) {
        return recyclingPointService.getPoint(pointId);
    }
}

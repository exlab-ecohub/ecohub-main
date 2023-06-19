package team.exlab.ecohub.recyclingpoint.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import team.exlab.ecohub.recyclingpoint.RecyclingPointService;
import team.exlab.ecohub.recyclingpoint.dto.RecyclingPointDto;
import team.exlab.ecohub.recyclingpoint.dto.RecyclingPointPartInfoDto;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/recycling-points")
@RequiredArgsConstructor
public class RecyclingPointController {
    private final RecyclingPointService recyclingPointService;

    @GetMapping
    public List<RecyclingPointPartInfoDto> getPoints(@RequestParam(required = false) Set<String> types,
                                                     @RequestParam(required = false, defaultValue = "0") Integer from,
                                                     @RequestParam(required = false, defaultValue = "0") Integer size) {
        return recyclingPointService.getPoints(types, from, size);
    }

    @GetMapping("/{pointId}")
    public RecyclingPointDto getPoint(@PathVariable Long pointId) {
        return recyclingPointService.getPoint(pointId);
    }
}

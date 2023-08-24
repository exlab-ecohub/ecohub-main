package team.exlab.ecohub.recyclingpoint.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import team.exlab.ecohub.recyclingpoint.RecyclingPointService;
import team.exlab.ecohub.recyclingpoint.dto.RecyclingPointDto;

import javax.validation.Valid;

@RestController
@RequestMapping("/admin/recycling-points")
@RequiredArgsConstructor
public class AdminRecyclingPointController {
    private final RecyclingPointService recyclingPointService;

    @PostMapping
    public RecyclingPointDto createPoint(@Valid @RequestBody RecyclingPointDto recyclingPoint) {
        return recyclingPointService.createPoint(recyclingPoint);
    }

    @PatchMapping("/{pointId}")
    public RecyclingPointDto updatePoint(@PathVariable Long pointId,
                                         @Valid @RequestBody RecyclingPointDto updatedPoint) {
        return recyclingPointService.updatePoint(pointId, updatedPoint);
    }

    @DeleteMapping("/{pointId}")
    public void deletePoint(@PathVariable Long pointId) {
        recyclingPointService.deletePoint(pointId);
    }
}

package team.exlab.ecohub.recyclingpoint.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import team.exlab.ecohub.recyclingpoint.RecyclingPointService;
import team.exlab.ecohub.recyclingpoint.dto.RecyclingPointDto;

@RestController
@RequestMapping("/admin/recycling-points")
@RequiredArgsConstructor
public class AdminRecyclingPointController {
    private final RecyclingPointService recyclingPointService;

    @PostMapping
    public RecyclingPointDto createPoint(@RequestBody RecyclingPointDto recyclingPoint) {
        printAuthenticationParams();
        return recyclingPointService.createPoint(recyclingPoint);
    }

    @GetMapping
    public String get() {
        printAuthenticationParams();
        return "GetResponse";
    }

    @PatchMapping("/{pointId}")
    public RecyclingPointDto updatePoint(@PathVariable Long pointId,
                                         @RequestBody RecyclingPointDto updatedPoint) {
        return recyclingPointService.updatePoint(pointId, updatedPoint);
    }

    @DeleteMapping("/{pointId}")
    public void deletePoint(@PathVariable Long pointId) {
        recyclingPointService.deletePoint(pointId);
    }

    private void printAuthenticationParams(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        System.out.println(auth.getAuthorities());
        System.out.println(auth.getName());
        System.out.println(auth.getPrincipal());
        System.out.println(auth.getCredentials());
        System.out.println(auth.getDetails());
        auth.getAuthorities().forEach(System.out::println);
    }
}

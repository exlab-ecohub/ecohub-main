package team.exlab.ecohub.swagger;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/swagger-download")
@RequiredArgsConstructor
public class SwaggerController {
    private final SwaggerService swaggerService;

    @GetMapping
    public ResponseEntity<String> getDocumentation() {
        return swaggerService.getDocumentation();
    }
}

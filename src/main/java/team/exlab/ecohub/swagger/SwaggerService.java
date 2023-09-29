package team.exlab.ecohub.swagger;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

@Slf4j
@Service
@RequiredArgsConstructor
public class SwaggerService {
    @Value("${app.swagger.path}")
    private String swaggerPath;

    public ResponseEntity<String> getDocumentation() {
        try (FileReader fileReader = new FileReader(swaggerPath);
             BufferedReader bufferedReader = new BufferedReader(fileReader)) {
            StringBuilder builder = new StringBuilder();
            bufferedReader.lines().forEach(x -> {
                builder.append(x);
                builder.append("\n");
            });
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_TYPE, "application/yaml");
            return new ResponseEntity<>(builder.toString(), headers, HttpStatus.OK);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}

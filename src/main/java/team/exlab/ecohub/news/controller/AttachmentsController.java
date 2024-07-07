package team.exlab.ecohub.news.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import team.exlab.ecohub.news.service.NewsItemService;

@RestController
@RequestMapping("/images")
@RequiredArgsConstructor
public class AttachmentsController {
    private final NewsItemService newsItemService;

    @GetMapping(value = "/{fileName}", produces = MediaType.IMAGE_JPEG_VALUE)
    public byte[] getAttachment(@PathVariable String fileName) {
        return newsItemService.getAttachment(fileName);
    }
}

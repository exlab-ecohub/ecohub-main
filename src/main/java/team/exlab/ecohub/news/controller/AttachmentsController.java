package team.exlab.ecohub.news.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import team.exlab.ecohub.news.service.NewsItemService;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequiredArgsConstructor
public class AttachmentsController {
    private final NewsItemService newsItemService;
    @GetMapping(value = "/E:/Git-projects/ecohub-main/files/news/images/**", produces = MediaType.IMAGE_JPEG_VALUE)
    public byte[] getAttachment(HttpServletRequest request) {
        return newsItemService.getAttachment(request.getRequestURI());
    }
}

package team.exlab.ecohub.news.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import team.exlab.ecohub.news.dto.NewsItemDto;
import team.exlab.ecohub.news.service.NewsItemService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/news")
public class NewsController {
    private final NewsItemService newsItemService;

    @PostMapping
    public NewsItemDto createNewsItem(@RequestPart NewsItemDto newsItemDto, @RequestPart(required = false) MultipartFile image) {
        return newsItemService.addNewsItem(newsItemDto, image);
    }

    @PatchMapping("/{newsId}")
    public NewsItemDto editNewsItem(@PathVariable Long newsId, @RequestPart NewsItemDto newsItemDto, @RequestPart(required = false) MultipartFile image) {
        return newsItemService.editNewsItem(newsId, newsItemDto, image);
    }

    @GetMapping
    public List<NewsItemDto> getAllNews() {
        return newsItemService.getAllNews();
    }

    @GetMapping("/{newsId}")
    public NewsItemDto getOneNewsItem(@PathVariable Long newsId) {
        return newsItemService.getNewsItem(newsId);
    }

    @GetMapping(value = "/images/**", produces = MediaType.IMAGE_JPEG_VALUE)
    public byte[] getAttachment(HttpServletRequest request) {
        return newsItemService.getAttachment(request.getRequestURI());
    }
}

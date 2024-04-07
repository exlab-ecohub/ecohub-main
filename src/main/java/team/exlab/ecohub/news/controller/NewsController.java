package team.exlab.ecohub.news.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import team.exlab.ecohub.news.dto.NewsItemDto;
import team.exlab.ecohub.news.service.NewsItemService;
import team.exlab.ecohub.news.validation.ContainersNumber;
import team.exlab.ecohub.news.validation.ValidImage;

import java.util.List;

@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping("/news")
public class NewsController {
	private final NewsItemService newsItemService;

	@PostMapping
	public NewsItemDto createNewsItem(@ContainersNumber @RequestPart NewsItemDto newsItemDto, @ValidImage @RequestPart(required = false) MultipartFile image) {
		return newsItemService.addNewsItem(newsItemDto, image);
	}

	@PatchMapping("/{newsId}")
	public NewsItemDto editNewsItem(@PathVariable Long newsId, @ContainersNumber @RequestPart NewsItemDto newsItemDto, @ValidImage @RequestPart(required = false) MultipartFile image) {
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
}

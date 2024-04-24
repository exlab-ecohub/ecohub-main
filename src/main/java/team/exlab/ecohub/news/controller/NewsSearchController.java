package team.exlab.ecohub.news.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import team.exlab.ecohub.news.dto.NewsItemDto;
import team.exlab.ecohub.news.service.NewsItemService;

import java.util.Collection;

@RestController
@RequiredArgsConstructor
@RequestMapping("/search/news")
public class NewsSearchController {
	private final NewsItemService newsItemService;

	@GetMapping("/keywords")
	public Collection<String> getKeywords(@RequestParam(name = "keywordSubstring") String keywordSubstring) {
		return newsItemService.getKeywordsBySubstring(keywordSubstring);
	}

	@GetMapping
	public Collection<NewsItemDto> getNewsByKeyWords(@RequestParam(name = "keywords") String[] keywords) {
		return newsItemService.getAllNewsWithSpecifiedKeywords(keywords);
	}
}

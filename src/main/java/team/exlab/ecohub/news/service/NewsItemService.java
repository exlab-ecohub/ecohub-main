package team.exlab.ecohub.news.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import team.exlab.ecohub.news.dto.NewsItemDto;

import java.util.Collection;
import java.util.List;

@Service
public interface NewsItemService {
    NewsItemDto addNewsItem(NewsItemDto newsItemDto, MultipartFile attachment);
    NewsItemDto editNewsItem(Long newsId, NewsItemDto newsItemDto, MultipartFile attachment);
    List<NewsItemDto> getAllNews();
    NewsItemDto getNewsItem(Long newsId);
    byte[] getAttachment(String attachmentURI);

	Collection<String> getKeywordsBySubstring(String keywordSubstring);

	Collection<NewsItemDto> getAllNewsWithSpecifiedKeywords(String[] keywords);
}

package team.exlab.ecohub.news.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import team.exlab.ecohub.news.dto.NewsItemDto;
import team.exlab.ecohub.news.model.NewsItem;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Service
public interface NewsItemService {
    NewsItemDto addNewsItem(NewsItemDto newsItemDto, MultipartFile attachment);
    NewsItemDto editNewsItem(Long newsId, NewsItemDto newsItemDto, MultipartFile attachment);
    List<NewsItemDto> getAllNews();
    NewsItemDto getNewsItem(Long newsId);
    byte[] getAttachment(String attachmentURI);
}

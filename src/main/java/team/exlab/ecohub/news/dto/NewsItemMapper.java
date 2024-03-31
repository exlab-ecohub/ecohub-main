package team.exlab.ecohub.news.dto;

import team.exlab.ecohub.news.model.NewsItem;

public class NewsItemMapper {
    private NewsItemMapper() {
    }

    public static NewsItemDto toDto(NewsItem newsItem) {
        return NewsItemDto.builder()
                .header(newsItem.getHeader())
                .text(newsItem.getText())
                .publicationDate(newsItem.getPublicationDate())
                .linkToSource(newsItem.getLinkToSource())
                .imageAttachment(newsItem.getImageAttachment())
                .keywords(newsItem.getKeywords())
                .type(newsItem.getType())
                .displayed(newsItem.isDisplayed())
                .build();
    }

    public static NewsItem toNewsItem(NewsItemDto newsItemDto) {
        return NewsItem.builder()
                .header(newsItemDto.getHeader())
                .text(newsItemDto.getText())
                .publicationDate(newsItemDto.getPublicationDate())
                .imageAttachment(newsItemDto.getImageAttachment())
                .linkToSource(newsItemDto.getLinkToSource())
                .type(newsItemDto.getType())
                .displayed(newsItemDto.isDisplayed())
                .keywords(newsItemDto.getKeywords())
                .build();
    }
}

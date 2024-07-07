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
}

package team.exlab.ecohub.exception;

public class NewsItemNotFoundException extends RuntimeException {
    public NewsItemNotFoundException(Long newsId) {
        super(String.format("News with id = %s not found in database", newsId));
    }
}

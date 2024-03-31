package team.exlab.ecohub.news.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import team.exlab.ecohub.news.model.NewsItem;

@Repository
public interface NewsItemRepository extends JpaRepository<NewsItem, Long> {
}

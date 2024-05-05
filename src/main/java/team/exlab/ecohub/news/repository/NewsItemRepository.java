package team.exlab.ecohub.news.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import team.exlab.ecohub.news.model.ENewsItemType;
import team.exlab.ecohub.news.model.NewsItem;

import java.util.List;

@Repository
public interface NewsItemRepository extends JpaRepository<NewsItem, Long> {
	List<NewsItem> findAllByTypeOrderById(Pageable pageable, ENewsItemType type);
	List<NewsItem> findAllByDisplayedAndTypeOrderById(Pageable pageable, Boolean displayed, ENewsItemType type);
}

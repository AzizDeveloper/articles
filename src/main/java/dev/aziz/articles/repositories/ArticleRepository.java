package dev.aziz.articles.repositories;

import dev.aziz.articles.dtos.DailyArticleCountDto;
import dev.aziz.articles.entities.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {

    @Query("SELECT new dev.aziz.articles.dtos.DailyArticleCountDto(a.localDate, COUNT(a)) " +
            "FROM Article a " +
            "WHERE a.localDate >= :startDate AND a.localDate <= CURRENT_DATE " +
            "GROUP BY a.localDate " +
            "ORDER BY a.localDate ASC")
    List<DailyArticleCountDto> countArticlesByDay(@Param("startDate") LocalDate startDate);

}

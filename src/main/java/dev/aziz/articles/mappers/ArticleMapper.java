package dev.aziz.articles.mappers;

import dev.aziz.articles.dtos.ArticleViewDto;
import dev.aziz.articles.entities.Article;
import org.springframework.stereotype.Component;

@Component
public class ArticleMapper {

    public ArticleViewDto convertToViewDto(Article article) {
        return ArticleViewDto.builder()
                .id(article.getId())
                .title(article.getTitle())
                .username(article.getUser().getUsername())
                .content(article.getContent())
                .localDate(article.getLocalDate())
                .build();
    }

}

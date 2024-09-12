package dev.aziz.articles.services;

import dev.aziz.articles.dtos.ArticleDto;
import dev.aziz.articles.dtos.ArticleViewDto;
import dev.aziz.articles.dtos.DailyArticleCountDto;
import dev.aziz.articles.dtos.UserDto;
import dev.aziz.articles.entities.Article;
import dev.aziz.articles.entities.User;
import dev.aziz.articles.exceptions.AppException;
import dev.aziz.articles.mappers.ArticleMapper;
import dev.aziz.articles.repositories.ArticleRepository;
import dev.aziz.articles.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final UserRepository userRepository;
    private final ArticleMapper articleMapper;

    public Page<ArticleViewDto> getAllArticles(int page, int size,
                                        String sortField, String sortDirection) {
        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortField);
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Article> articles = articleRepository.findAll(pageable);

        Page<ArticleViewDto> articleDtos = articles.map(articleMapper::convertToViewDto);

        return articleDtos;
    }

    public ArticleViewDto saveArticle(ArticleDto articleDto, UserDto userDto) {
        User user = userRepository.findById(userDto.getId())
                .orElseThrow(() -> new AppException("User Not Found", HttpStatus.NOT_FOUND));
        Article article = Article.builder()
                .title(articleDto.getTitle())
                .content(articleDto.getContent())
                .user(user)
                .localDate(articleDto.getLocalDate())
                .build();
        Article savedArticle = articleRepository.save(article);

        ArticleViewDto articleViewDto = articleMapper.convertToViewDto(savedArticle);
        return articleViewDto;
    }

    public List<DailyArticleCountDto> getStats() {
        LocalDate today = LocalDate.now();
        LocalDate sevenDaysAgo = today.minusDays(7);
        return articleRepository.countArticlesByDay(sevenDaysAgo);
    }
}













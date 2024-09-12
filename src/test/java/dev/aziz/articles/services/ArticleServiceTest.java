package dev.aziz.articles.services;

import dev.aziz.articles.dtos.ArticleDto;
import dev.aziz.articles.dtos.ArticleViewDto;
import dev.aziz.articles.dtos.DailyArticleCountDto;
import dev.aziz.articles.dtos.UserDto;
import dev.aziz.articles.entities.Article;
import dev.aziz.articles.entities.Role;
import dev.aziz.articles.entities.User;
import dev.aziz.articles.mappers.ArticleMapper;
import dev.aziz.articles.repositories.ArticleRepository;
import dev.aziz.articles.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ArticleServiceTest {

    @InjectMocks
    private ArticleService articleService;

    @Mock
    private ArticleRepository articleRepository;

    @Mock
    private UserRepository userRepository;

    @Spy
    private ArticleMapper articleMapper;

    @Test
    void getAllArticlesTest() {
        // given
        User user = User.builder().id(1L).username("aziz").build();
        Article article = Article.builder()
                .title("Spring Boot")
                .content("Spring Boot content")
                .localDate(LocalDate.now())
                .user(user)
                .build();
        ArticleViewDto articleViewDto = ArticleViewDto.builder()
                .title(article.getTitle())
                .content(article.getContent())
                .username(article.getUser().getUsername())
                .build();

        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "title"));
        Page<Article> articlePage = new PageImpl<>(List.of(article), pageable, 1);

        // when
        when(articleRepository.findAll(pageable)).thenReturn(articlePage);
        when(articleMapper.convertToViewDto(article)).thenReturn(articleViewDto);

        Page<ArticleViewDto> result = articleService.getAllArticles(0, 10, "title", "ASC");

        // then
        assertAll(() -> {
            assertEquals(1, result.getTotalElements());
            assertEquals(articleViewDto.getTitle(), result.getContent().get(0).getTitle());
            assertEquals(articleViewDto.getContent(), result.getContent().get(0).getContent());
        });
    }

    @Test
    void saveArticleTest() {
        //given
        ArticleDto articleDto = ArticleDto.builder()
                .title("Spring")
                .content("Spring")
                .localDate(LocalDate.now())
                .build();
        UserDto userDto = UserDto.builder()
                .id(1L)
                .username("aziz")
                .email("aziz@gmail.com")
                .role(Role.USER)
                .build();
        User user = User.builder().id(1L).build();
        Article article = Article.builder()
                .title(articleDto.getTitle())
                .content(articleDto.getContent())
                .user(user)
                .localDate(articleDto.getLocalDate())
                .build();
        Article savedArticle = article;
        savedArticle.setId(1L);

        //when
        when(userRepository.findById(userDto.getId())).thenReturn(Optional.of(user));
        when(articleRepository.save(any((Article.class)))).thenReturn(savedArticle);

        ArticleViewDto resultArticleViewDto = articleService.saveArticle(articleDto, userDto);
        //then
        assertAll(() -> {
            assertEquals(articleDto.getTitle(), resultArticleViewDto.getTitle());
            assertEquals(articleDto.getContent(), resultArticleViewDto.getContent());
        });
    }

    @Test
    void getStatsTest() {
        //given
        LocalDate today = LocalDate.now();
        LocalDate startDate = today.minusDays(7);
        List<DailyArticleCountDto> stats = new ArrayList<>();
        stats.add(DailyArticleCountDto.builder().count(2L).date(startDate.plusDays(3)).build());
        stats.add(DailyArticleCountDto.builder().count(3L).date(startDate.plusDays(5)).build());
        stats.add(DailyArticleCountDto.builder().count(4L).date(startDate.plusDays(1)).build());

        //when
        when(articleRepository.countArticlesByDay(startDate)).thenReturn(stats);
        List<DailyArticleCountDto> resultStats = articleService.getStats();

        //then
        assertAll(() -> {
            assertEquals(stats.size(), resultStats.size());
            assertEquals(stats.get(0).getCount(), resultStats.get(0).getCount());
        });
    }
}

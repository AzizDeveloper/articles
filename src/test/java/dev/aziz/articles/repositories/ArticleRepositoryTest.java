package dev.aziz.articles.repositories;

import dev.aziz.articles.dtos.DailyArticleCountDto;
import dev.aziz.articles.entities.Article;
import dev.aziz.articles.entities.Role;
import dev.aziz.articles.entities.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@Transactional
@AutoConfigureTestDatabase
class ArticleRepositoryTest {

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    void testCountArticlesByDay() {
        // given
        LocalDate today = LocalDate.now();
        LocalDate startDate = today.minusDays(7);

        User user = User.builder()
                .username("lisandro")
                .email("lisandro@lisandro.com")
                .firstName("Lisandro")
                .lastName("Martinez")
                .role(Role.ADMIN)
                .password("lisandro")
                .build();

        Article article1 = Article.builder()
                .title("Spring Boot")
                .content("Content 1")
                .localDate(today)
                .user(user)
                .build();
        Article article2 = Article.builder()
                .title("Spring Data JPA")
                .content("Content 2")
                .localDate(today)
                .user(user)
                .build();
        Article article3 = Article.builder()
                .title("Spring Security")
                .content("Content 3")
                .localDate(today)
                .user(user)
                .build();
        Article article4 = Article.builder()
                .title("Spring Cloud")
                .content("Content 4")
                .localDate(today)
                .user(user)
                .build();
        userRepository.save(user);
        articleRepository.save(article1);
        articleRepository.save(article2);
        articleRepository.save(article3);
        articleRepository.save(article4);

        // when
        List<DailyArticleCountDto> result = articleRepository.countArticlesByDay(startDate);

        // then
        assertThat(result).hasSize(1);
        assertThat(result).extracting("date")
                .containsExactly(today);

        assertThat(result).extracting("count")
                .containsExactly(4L);

    }
}


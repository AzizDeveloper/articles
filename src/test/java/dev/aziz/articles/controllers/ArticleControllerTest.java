package dev.aziz.articles.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.aziz.articles.dtos.ArticleDto;
import dev.aziz.articles.dtos.ArticleViewDto;
import dev.aziz.articles.dtos.DailyArticleCountDto;
import dev.aziz.articles.dtos.UserDto;
import dev.aziz.articles.entities.Role;
import dev.aziz.articles.entities.User;
import dev.aziz.articles.repositories.UserRepository;
import dev.aziz.articles.services.ArticleService;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ArticleController.class)
@AutoConfigureMockMvc(addFilters = false)
class ArticleControllerTest {

    @Autowired
    public MockMvc mockMvc;

    @MockBean
    public ArticleService articleService;

    @Autowired
    public ObjectMapper objectMapper;

    @Mock
    private UserRepository userRepository;

    @Test
    void getArticlesTest() throws Exception {
        //given
        ArticleViewDto springDto = ArticleViewDto.builder()
                .id(1L)
                .title("Spring")
                .content("It's about Spring Boot 3")
                .localDate(LocalDate.now().minusDays(3))
                .build();

        ArticleViewDto reactDto = ArticleViewDto.builder()
                .id(2L)
                .title("React JS")
                .content("It's about React JS")
                .localDate(LocalDate.now().minusDays(2))
                .build();

        List<ArticleViewDto> articleDtosList = List.of(springDto, reactDto);
        Page<ArticleViewDto> articleViewDtos = new PageImpl<>(articleDtosList);

        //when
        when(articleService.getAllArticles(0, 10, "localDate", "ASC")).thenReturn(articleViewDtos);

        //then
        mockMvc.perform(get("/articles")
                        .param("page", "0")
                        .param("size", "10")
                        .param("sortField", "localDate")
                        .param("sortDirection", "ASC"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].title").value("Spring"))
                .andExpect(jsonPath("$.content[1].title").value("React JS"));

    }


    // This test doesn't work, I could not find the error.
    @WithMockUser(username = "aziz", roles = {"USER"})
    @Test
    void addArticleTest() throws Exception {
        //given
        ArticleDto springDto = ArticleDto.builder()
                .title("Spring")
                .content("It's about Spring Boot 3")
                .localDate(LocalDate.of(2024, 9, 16))
                .build();
        UserDto userDto = UserDto.builder()
                .id(1L)
                .username("aziz")
                .email("aziz@gmail.com")
                .role(Role.USER)
                .build();
        User user = User.builder().id(1L).build();
        ArticleViewDto savedArticle = ArticleViewDto.builder()
                .id(1L)
                .title("Spring")
                .content("It's about Spring Boot 3")
                .localDate(LocalDate.of(2024, 9, 16))
                .username(userDto.getUsername())
                .build();

        //when
        when(userRepository.findById(userDto.getId())).thenReturn(Optional.of(user));
        when(articleService.saveArticle(springDto, userDto)).thenReturn(savedArticle);

        System.out.println("savedArticle = " + savedArticle);

        //then
        String content = objectMapper.writeValueAsString(springDto);
        System.out.println("content = " + content);
        mockMvc.perform(post("/articles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
                        .with(user("aziz").roles("USER"))
                )
                .andExpect(status().isOk())
                .andDo(result -> System.out.println("Response Body: " + result.getResponse().getContentAsString()))
                .andExpect(jsonPath("$.title").value("Spring"));
    }

    @WithMockUser(username = "aziz", roles = {"ADMIN"})
    @Test
    void getStatsTest() throws Exception {
        //given
        List<DailyArticleCountDto> data = new ArrayList<>();
        data.add(new DailyArticleCountDto(LocalDate.of(2024, 9, 10), 3L));
        data.add(new DailyArticleCountDto(LocalDate.of(2024, 9, 11), 4L));
        data.add(new DailyArticleCountDto(LocalDate.of(2024, 9, 8), 5L));

        //when
        when(articleService.getStats()).thenReturn(data);

        //then
        mockMvc.perform(get("/articles/stats").with(user("aziz").roles("ADMIN")))
                .andExpect(status().isOk())
                .andDo(result -> System.out.println("Response Body: " + result.getResponse().getContentAsString()))
                .andExpect(jsonPath("$[0].date").value("2024-09-10"))  // Check the date
                .andExpect(jsonPath("$[0].count").value(3))             // Check the count
                .andExpect(jsonPath("$[1].date").value("2024-09-11"))  // Check the date
                .andExpect(jsonPath("$[1].count").value(4))             // Check the count
                .andExpect(jsonPath("$[2].date").value("2024-09-08"))  // Check the date
                .andExpect(jsonPath("$[2].count").value(5));
    }

}

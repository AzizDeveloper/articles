package dev.aziz.articles.controllers;

import dev.aziz.articles.dtos.ArticleDto;
import dev.aziz.articles.dtos.ArticleViewDto;
import dev.aziz.articles.dtos.DailyArticleCountDto;
import dev.aziz.articles.dtos.UserDto;
import dev.aziz.articles.entities.Article;
import dev.aziz.articles.services.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/articles")
public class ArticlesController {

    private final ArticleService articleService;

    @GetMapping
    public Page<ArticleViewDto> getArticles(@RequestParam(defaultValue = "0") int page,
                                            @RequestParam(defaultValue = "10") int size,
                                            @RequestParam(defaultValue = "id") String sortField,
                                            @RequestParam(defaultValue = "ASC") String sortDirection) {
        return articleService.getAllArticles(page, size, sortField, sortDirection);
    }

    @PostMapping
    public ArticleViewDto addArticle(@AuthenticationPrincipal UserDto userDto, @RequestBody ArticleDto articleDto) {
        return articleService.saveArticle(articleDto, userDto);
    }

    @GetMapping("/stats")
    public List<DailyArticleCountDto> getStats() {
        return articleService.getStats();
    }

}

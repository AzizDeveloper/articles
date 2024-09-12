package dev.aziz.articles.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ArticleViewDto {

    private Long id;

    private String title;

    private String username;

    private String content;

    private LocalDate localDate;

}
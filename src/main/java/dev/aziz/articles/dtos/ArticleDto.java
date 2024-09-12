package dev.aziz.articles.dtos;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ArticleDto {

    private Long id;

    @NotNull(message = "Title should not be empty or greater than 100 characters")
    @Size(max = 100)
    private String title;

    @NotNull(message = "Content should not be empty")
    private String content;

    @NotNull(message = "Date for publishing should not be empty")
    @FutureOrPresent
    private LocalDate localDate;

}

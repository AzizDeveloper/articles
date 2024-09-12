package dev.aziz.articles.dtos;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Builder
@Data
public class DailyArticleCountDto {

    private LocalDate date;
    private Long count;

    public DailyArticleCountDto(LocalDate date, Long count) {
        this.date = date;
        this.count = count;
    }

    public DailyArticleCountDto() {
    }

}

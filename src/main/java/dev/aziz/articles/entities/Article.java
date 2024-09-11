package dev.aziz.articles.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "articles")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // TODO:  max 100 characters
    @NotNull(message = "Title should not be empty")
    private String title;

    @NotNull(message = "Author should not be empty")
    private String author;

    @NotNull(message = "Content should not be empty")
    private String content;

    // TODO: The publishing date should bind to ISO 8601 format.
    @NotNull(message = "Date for publishing should not be empty")
    private LocalDate localDate;

}

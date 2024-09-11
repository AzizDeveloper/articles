package dev.aziz.articles.dtos;

import dev.aziz.articles.entities.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class UserSummaryDto {

    private Long id;
    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private Role role;

}

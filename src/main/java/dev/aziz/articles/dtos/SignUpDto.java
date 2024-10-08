package dev.aziz.articles.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class SignUpDto {

    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private String role;
    private char[] password;

}

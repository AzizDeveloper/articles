package dev.aziz.articles.mappers;

import dev.aziz.articles.dtos.SignUpDto;
import dev.aziz.articles.dtos.UserDto;
import dev.aziz.articles.dtos.UserSummaryDto;
import dev.aziz.articles.entities.User;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserMapper {

    public UserDto toUserDto(User user) {
        if (user == null) {
            return null;
        } else {
            UserDto.UserDtoBuilder userDto = UserDto.builder();
            userDto.id(user.getId());
            userDto.firstName(user.getFirstName());
            userDto.lastName(user.getLastName());
            userDto.email(user.getEmail());
            userDto.username(user.getUsername());
            userDto.role(user.getRole());
            return userDto.build();
        }
    }

    public User signUpToUser(SignUpDto signUpDto) {
        if (signUpDto == null) {
            return null;
        } else {
            User.UserBuilder user = User.builder();
            user.firstName(signUpDto.getFirstName());
            user.lastName(signUpDto.getLastName());
            user.email(signUpDto.getEmail());
            user.username(signUpDto.getUsername());
            return user.build();
        }
    }

    public UserSummaryDto toUserSummaryDto(User user) {
        if (user == null) {
            return null;
        } else {
            UserSummaryDto.UserSummaryDtoBuilder userSummaryDto = UserSummaryDto.builder();
            userSummaryDto.id(user.getId());
            userSummaryDto.firstName(user.getFirstName());
            userSummaryDto.lastName(user.getLastName());
            userSummaryDto.email(user.getEmail());
            userSummaryDto.username(user.getUsername());
            userSummaryDto.role(user.getRole());
            return userSummaryDto.build();
        }
    }

    public List<UserDto> usersToUserDtos(List<User> users) {
        if (users == null) {
            return null;
        }
        return users.stream()
                .map(this::toUserDto)
                .collect(Collectors.toList());
    }

    public List<UserSummaryDto> usersToUserSummaryDtos(List<User> users) {
        if (users == null) {
            return null;
        }
        return users.stream()
                .map(this::toUserSummaryDto)
                .collect(Collectors.toList());
    }

}

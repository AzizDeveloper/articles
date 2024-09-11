package dev.aziz.articles.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

//    private final UserService userService;
//
//    @GetMapping
//    public ResponseEntity<List<UserSummaryDto>> getAllUsers() {
//        return ResponseEntity.ok(userService.getAllUsers());
//    }

}

package com.example.dockerdemo.demo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.swing.text.html.Option;
import java.util.Optional;

@RestController
public class MainController {

    private final UserService userService;
    public MainController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public String hello() {
        return "Hello, Docker!";
    }

    @GetMapping("/user")
    public User user(@RequestParam String id) {
        Optional<User> user = Optional.ofNullable(userService.getUser(id));
        return user.orElse(null);
    }

}

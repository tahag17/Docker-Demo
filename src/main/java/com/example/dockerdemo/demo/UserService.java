package com.example.dockerdemo.demo;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final List<User> users = new ArrayList<>();

    public UserService() {
        for (int i = 0; i < 10; i++) {
            users.add(new User(String.valueOf(i),
                    "User" + i,
                    "user" + i + "@gmail.com"
            ));
        }
    }

    public User getUser(String id) {
        return users.stream().
                filter(user -> user.getId().equals(id)).findFirst().orElse(null);
    }

}

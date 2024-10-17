package org.connerretttech.server.controllers;


import org.connerretttech.server.models.User;
import org.connerretttech.server.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:5173/") //Allows front-end to interact with back-end
@RequestMapping("/api/user") //base request call front end will fetch to
public class UserController {
@Autowired //When class is called, automatically create object and get users in repositories
    UserRepository userRepository;

    private static final String userSessionKey = "user";

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers(){ //Any mapping including the api user URL will find all users and return back a response entity (HTTP status)
        List<User> users = userRepository.findAll();
                return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable int id) {
        Optional<User> user = userRepository.findById(id);
        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
}

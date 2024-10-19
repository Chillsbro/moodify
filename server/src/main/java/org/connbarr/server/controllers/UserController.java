package org.connbarr.server.controllers;


import jakarta.validation.Valid;
import org.connbarr.server.models.User;
import org.connbarr.server.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:5173/") //Allows front-end to interact with back-end (allows traffic from specified URL)
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

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateUser(@PathVariable int id, @RequestBody @Valid User updatedUser, BindingResult result) {
        if (result.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            for (FieldError error : result.getFieldErrors()) {
                errors.put(error.getField(), error.getDefaultMessage());
            }
            return ResponseEntity.unprocessableEntity().body(errors);
        }

        Optional<User> existingUser = userRepository.findById(id);
        if (existingUser.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        User existingUserToUpdate = existingUser.get();
        existingUserToUpdate.setUsername(updatedUser.getUserName());
        existingUserToUpdate.setEmail(updatedUser.getEmail());
        if (!updatedUser.isPasswordEmpty()) {
            existingUserToUpdate.setPassword(User.getEncoder().encode(updatedUser.getPassword()));
        }

        userRepository.save(existingUserToUpdate);
        return ResponseEntity.ok(existingUserToUpdate);
    }
}

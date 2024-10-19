package org.connbarr.server.controllers;

import java.util.Optional;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.connbarr.server.dto.LoginFormDTO;
import org.connbarr.server.dto.RegisterFormDTO;
import org.connbarr.server.models.User;
import org.connbarr.server.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:5173")  //Default React local host
@RequestMapping("/api")
public class ApiController {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    @Autowired
    public ApiController(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    private static final String userSessionKey = "user";
    public User getUserFromSession(HttpSession session) {
        Integer userId = (Integer) session.getAttribute(userSessionKey);
        if (userId == null) {
            return null;
        }
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            return null;
        }
        return user.get();
    }

    private static void setUserInSession(HttpSession session, User user) {
        session.setAttribute(userSessionKey, user.getId());
    }


    public ResponseEntity<User> getCurrentUser(HttpSession session) {
        User user = getUserFromSession(session);

        if (user == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping("/currentUserId")
    public ResponseEntity<Integer> getCurrentUserId (HttpSession session) {
        User user = getUserFromSession(session);
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        return new ResponseEntity<>(user.getId(),HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody @Valid RegisterFormDTO registerFormDTO, Errors errors, HttpServletRequest request) {
        if (errors.hasErrors()) {
            System.out.println(errors.getAllErrors());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        User existingUser = userRepository.findByUsernameIgnoreCase(registerFormDTO.getUsername());

        if (existingUser != null) {
            return new ResponseEntity<String>("Username already exists", HttpStatus.CONFLICT);
        }

        String password = registerFormDTO.getPassword();
        String verifyPassword = registerFormDTO.getVerifyPassword();

        if (!password.equals(verifyPassword)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        User newUser = new User(registerFormDTO.getUsername(), registerFormDTO.getEmail(),passwordEncoder.encode(password));
        userRepository.save(newUser);

        setUserInSession(request.getSession(), newUser);

        return new ResponseEntity<>(newUser, HttpStatus.CREATED);
    }

    @GetMapping("/login")
    public ResponseEntity<String> displayLoginForm() {
        return new ResponseEntity<>("Please log in", HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<User> processLoginForm(@RequestBody @Valid LoginFormDTO loginFormDTO, Errors errors, HttpServletRequest request) {
        if (errors.hasErrors()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        User theUser = userRepository.findByUsernameIgnoreCase(loginFormDTO.getUsername());

        if (theUser == null || !theUser.isMatchingPassword(loginFormDTO.getPassword())) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        setUserInSession(request.getSession(), theUser);

        System.out.println("User authenticated: " + theUser.getUserName());

        return new ResponseEntity<>(theUser, HttpStatus.OK);
    }

    @GetMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request) {
        request.getSession().invalidate();
        return new ResponseEntity<>("Logout successful", HttpStatus.OK);
    }
}

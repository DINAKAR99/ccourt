package com.example.demo.controller;

import java.util.HashSet;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.Role;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;

@RestController
@RequestMapping("/public")
public class UserController {
    @Autowired
    private UserRepository repo;
    @Autowired
    private PasswordEncoder pass;

    @GetMapping("/")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(repo.findAll());
    }

    @GetMapping("/{name}")
    public ResponseEntity<User> getUserByName(@PathVariable String name) {

        // User orElse = users.stream().filter(s ->
        // s.getUsername().equals(name)).findAny().orElse(null);

        return ResponseEntity.ok(repo.findByUserName(name));

    }

    @PostMapping("/create")
    public ResponseEntity<?> createUser(@RequestBody User us, HttpServletRequest request) {
        System.out.println("edefef");
        String encode = pass.encode(us.getPassword());
        us.setRealPassword(us.getPassword());
        us.setPassword(encode);
        us.setCreatedIpAddress(request.getRemoteAddr());
        Role role = new Role();
        role.setRoleName("USER");
        us.setRole(role);
        return ResponseEntity.ok(repo.save(us));

    }

    @GetMapping("/api/{name}")
    public ResponseEntity<String> getExample(@PathVariable String name) {
        String responseBody = "dinakar";
        if (name.equals("dinakar")) {

            return new ResponseEntity<>(responseBody, HttpStatus.OK);
        } else {

            return new ResponseEntity<>("no user found", HttpStatus.NOT_FOUND);
        }
    }

}

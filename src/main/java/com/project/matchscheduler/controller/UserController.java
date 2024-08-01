package com.project.matchscheduler.controller;

import com.project.matchscheduler.model.User;
import com.project.matchscheduler.model.UserType;
import com.project.matchscheduler.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable Long id) {
        Optional<User> user = userService.getUserById(id);
        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build()).getBody();
    }

    @PostMapping
    public User createUser(@RequestBody User user) {
        return userService.createUser(user);
    }

    @PostMapping("/admin")
    public ResponseEntity<User> createAdminUser(@RequestBody User user, @RequestHeader("admin-id") Long adminId) {
        Optional<User> adminUser = userService.getUserById(adminId);
        if (adminUser.isPresent() && adminUser.get().getUserType() == UserType.ADMIN) {
            User createdUser = userService.createAdminUser(user);
            return ResponseEntity.ok(createdUser);
        } else {
            return ResponseEntity.status(403).build(); // Forbidden
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User userDetails) {
        try {
            User updatedUser = userService.updateUser(id, userDetails);
            return ResponseEntity.ok(updatedUser);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}

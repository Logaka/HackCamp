package org.example.hackcamp.controllers;

import org.example.hackcamp.models.User;
import org.example.hackcamp.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.concurrent.ExecutionException;

@RestController
public class UserController {
    public UserService userService;

    public UserController(UserService userService){
        this.userService = userService;
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping("/create")
    public String createUser(@RequestBody User user) throws ExecutionException, InterruptedException {
        return userService.createUser(user);
    }

    @GetMapping("/get")
    public User getUser(@RequestParam String documentId) throws ExecutionException, InterruptedException {
        return userService.getUser(documentId);
    }

    @PutMapping("/update")
    public String updateUser(@RequestBody User user){
        return userService.updateUser(user);
    }

    @PutMapping("/delete")
    public String deleteUser(@RequestParam String documentId){
        return userService.deleteUser(documentId);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody Map<String, String> credentials) {
        String email = credentials.get("email");
        String password = credentials.get("password");

        try {
            boolean isAuthenticated = userService.authenticateUser(email, password);
            if (isAuthenticated) {
                System.out.println("Login successful");
                return ResponseEntity.ok("Login successful");
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred during login");
        }
    }



    @GetMapping("/test")
    public ResponseEntity<String> testGetEndpoint(){ return ResponseEntity.ok("Test Get Endpoint is ready");}

}

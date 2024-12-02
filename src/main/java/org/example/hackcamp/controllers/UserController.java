package org.example.hackcamp.controllers;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import lombok.extern.java.Log;
import org.example.hackcamp.models.Hackathon;
import org.example.hackcamp.models.RegistrationRequest;
import org.example.hackcamp.models.User;
import org.example.hackcamp.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

@RestController
public class UserController {
    public UserService userService;

    public UserController(UserService userService) {
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
    public String updateUser(@RequestBody User user) {
        return userService.updateUser(user);
    }

    @PutMapping("/delete")
    public String deleteUser(@RequestParam String documentId) {
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
                System.out.println("Invalid credentials");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
            }
        } catch (Exception e) {
            System.out.println(e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred during login");
        }
    }


    @PostMapping("/hackathon/create")
    public String createHackathon(@RequestBody Hackathon hackathon) {
        try {
            return userService.createHackathon(hackathon);
        } catch (ExecutionException | InterruptedException e) {
            return "Error creating hackathon: " + e.getMessage();
        }
    }

    @GetMapping("/hackathons")
    public List<Hackathon> getAllHackathons() throws ExecutionException, InterruptedException {
        return userService.getAllHackathons();

    }

    @GetMapping("/hackathon/{id}")
    public Hackathon getHackathonById(@PathVariable String id) throws ExecutionException, InterruptedException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        DocumentReference documentReference = dbFirestore.collection("hackathons").document(id);
        ApiFuture<DocumentSnapshot> future = documentReference.get();
        DocumentSnapshot document = future.get();

        if (document.exists()) {
            return document.toObject(Hackathon.class); // Преобразуем документ в объект Hackathon
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Hackathon not found");
        }
    }


    @PostMapping("/registrations")
    public ResponseEntity<String> registerParticipant(@RequestBody RegistrationRequest registrationRequest) {
        Firestore dbFirestore = FirestoreClient.getFirestore();

        ApiFuture<WriteResult> collectionsApiFuture = dbFirestore
                .collection("registrations")
                .document(UUID.randomUUID().toString())
                .set(registrationRequest);

        try {
            collectionsApiFuture.get();
            return ResponseEntity.ok("Registration successful!");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to register.");
        }
    }

    @GetMapping("/registration")
    public ResponseEntity<List<RegistrationRequest>> getAllRegistrations() {
        Firestore dbFirestore = FirestoreClient.getFirestore();

        try {
            ApiFuture<QuerySnapshot> future = dbFirestore.collection("registrations").get();
            List<QueryDocumentSnapshot> documents = future.get().getDocuments();

            // Преобразуем документы в список объектов RegistrationResponse
            List<RegistrationRequest> registrations = new ArrayList<>();
            for (QueryDocumentSnapshot document : documents) {
                RegistrationRequest registration = document.toObject(RegistrationRequest.class);
                registrations.add(registration);
            }

            return ResponseEntity.ok(registrations);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/test")
    public ResponseEntity<String> testGetEndpoint() {
        return ResponseEntity.ok("Test Get Endpoint is ready");
    }

}

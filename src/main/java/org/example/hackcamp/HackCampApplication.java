package org.example.hackcamp;


import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Objects;


@SpringBootApplication
public class HackCampApplication {


    public static void main(String[] args) throws IOException {
        ClassLoader classLoader = HackCampApplication.class.getClassLoader();


        FileInputStream serviceAccount;
        {
            try {
                serviceAccount = new FileInputStream(
                        Objects.requireNonNull(HackCampApplication.class.getClassLoader().getResource("ServiceAccountKey.json")).getFile()
                );
            } catch (FileNotFoundException e) {
                throw new RuntimeException("ServiceAccountKey.json not found", e);
            }
        }


        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .build();

        FirebaseApp.initializeApp(options);


        SpringApplication.run(HackCampApplication.class, args);
    }

}

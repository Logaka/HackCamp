package org.example.hackcamp.service;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import org.example.hackcamp.models.Hackathon;
import org.example.hackcamp.models.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
public class UserService {


    public String createUser(User user) throws ExecutionException, InterruptedException {
        Firestore dbFireStore = FirestoreClient.getFirestore();
        dbFireStore.collection("crud-user").document(user.getName()).set(user);
        return "User created successfully!";
    }

    public User getUser(String documentId) throws ExecutionException, InterruptedException {
        Firestore dbFireStore = FirestoreClient.getFirestore();
        DocumentReference documentReference = dbFireStore.collection("crud-user").document(documentId);
        ApiFuture<DocumentSnapshot> future = documentReference.get();
        DocumentSnapshot document = future.get();
        User user;
        if (document.exists()){
            user = document.toObject(User.class);
            return user;
        }
        return null;
    }

    public String updateUser(User user) {

        return "";
    }

    public String deleteUser(String documentId) {
        Firestore dbFireStore = FirestoreClient.getFirestore();
        dbFireStore.collection("crud_user").document(documentId).delete();

        return "Successfully deleted" + documentId;
    }

    public boolean authenticateUser(String email, String password) throws ExecutionException, InterruptedException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        CollectionReference users = dbFirestore.collection("crud-user");

        Query query = users.whereEqualTo("email", email);
        ApiFuture<QuerySnapshot> querySnapshot = query.get();

        List<QueryDocumentSnapshot> documents = querySnapshot.get().getDocuments();
        if (!documents.isEmpty()) {
            for (DocumentSnapshot document : documents) {
                User user = document.toObject(User.class);
                if (user.getPassword().equals(password)) {
                    return true; // Authentication successful
                }
            }
        }
        return false; // Authentication failed
    }

    public String createHackathon(Hackathon hackathon) throws ExecutionException, InterruptedException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        ApiFuture<WriteResult> collectionsApiFuture =
                dbFirestore.collection("hackathons").document(hackathon.getTitle()).set(hackathon);
        System.out.println("Hackathon created successfully at: " + collectionsApiFuture.get().getUpdateTime());
        return "Hackathon created successfully at: " + collectionsApiFuture.get().getUpdateTime();
    }

    public List<Hackathon> getAllHackathons() throws ExecutionException, InterruptedException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        ApiFuture<QuerySnapshot> future = dbFirestore.collection("hackathons").get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();
        List<Hackathon> hackathons = new ArrayList<>();

        for (QueryDocumentSnapshot document : documents) {
            hackathons.add(document.toObject(Hackathon.class));
        }

        return hackathons;
    }
}

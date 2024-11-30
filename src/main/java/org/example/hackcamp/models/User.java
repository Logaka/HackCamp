package org.example.hackcamp.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class User {
    private String document_id;
    private String name;
    private String email;
    private String password;
}

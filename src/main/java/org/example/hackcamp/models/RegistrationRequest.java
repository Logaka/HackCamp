package org.example.hackcamp.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegistrationRequest {
    private String firstName;
    private String lastName;
    private String email;
    private String teamName;
    private String teamLeader;
    private int memberCount;
    private String memberNames;
    private String eventId;
}


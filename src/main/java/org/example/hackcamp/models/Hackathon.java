package org.example.hackcamp.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Hackathon {
    private String title;
    private String location;
    private String duration;
    private String description;
    private String theme;
    private String prizes;
    private String registrationDeadline;
}

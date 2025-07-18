package com.pcmc.smartsarathi.model;


import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class LoginDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(unique = true)
    private String username;


    private String ipAddress;

    private LocalDateTime loginAt;

    @ManyToOne
    @JsonIgnore
    private User created;

    @Override
    public String toString() {
        return "LoginDetails{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", issuedAt=" + loginAt +
                '}';
    }

}
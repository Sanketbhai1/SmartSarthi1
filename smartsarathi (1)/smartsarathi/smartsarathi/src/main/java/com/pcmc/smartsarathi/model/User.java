package com.pcmc.smartsarathi.model;


import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@Entity
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank(message = "Username is mandatory")
    @Size(max = 100, message = "Username cannot exceed 100 characters")
    @Column(nullable = false, unique = true)
    private String username;

    @NotBlank(message = "Name is mandatory")
    @Size(max = 100, message = "Name cannot exceed 100 characters")
    @Column(nullable = false)
    private String name;


    @NotBlank(message = "Password is mandatory")
    private String password;


    @NotBlank(message = "Role is mandatory")
    @Column(nullable = false)
    private String roles;

    private boolean status;

    @ManyToOne
    @JsonIgnore
    private User createdUser;

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", roles='" + roles + '\'' +
                ", status=" + status +
                '}';
    }

    @ManyToOne
    @JsonIgnore
    private User modifiedUser;

    private LocalDate addedTime;
    private LocalDate modifiedTime;


}

package com.pcmc.smartsarathi.config;

import com.pcmc.smartsarathi.model.User;
import com.pcmc.smartsarathi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;


@Configuration
public class DefaultDataInitializer {

    @Autowired
    private PasswordEncoder encoder;


    @Bean
    public ApplicationRunner initializer(UserRepository userRepository) {
        return args -> {
            String superUsername = "lokesh.c@gmail.com";
            String superPassword = "super123";
            String superRole = "SUPER_ADMIN";

            userRepository.findByUsername(superUsername).ifPresentOrElse(admin -> {
                System.out.println("SUPER ADMIN ALREADY EXIST.");
                System.out.println("USERNAME: " + superUsername);
                System.out.println("PASSWORD: " + superPassword);
            }, () -> {
                try {
                    System.err.println("SUPER ADMIN NOT FOUND\n");
                    System.out.println("CREATING SUPER ADMIN FOR YOUR APPLICATION...");
                    User user = new User();
                    user.setUsername(superUsername);
                    user.setPassword(encoder.encode(superPassword));
                    user.setRoles(superRole);
                    user.setCreatedUser(user);
                    user.setModifiedUser(user);
                    user.setAddedTime(LocalDate.now());
                    user.setModifiedTime(LocalDate.now());
                    user.setStatus(true);
                    user.setName("lokesh");
                    userRepository.save(user);


                    System.out.println("SUPER ADMIN CREATED SUCCESS..");
                    System.out.println("USERNAME: " + superUsername);
                    System.out.println("PASSWORD: " + superPassword);
                } catch (Exception e) {
                    return;
                }

            });
        };
    }
}

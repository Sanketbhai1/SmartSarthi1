package com.pcmc.smartsarathi.repository;

import com.pcmc.smartsarathi.model.LoginDetails;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LoginDetailsRepository extends JpaRepository<LoginDetails, Long> {
    Optional<LoginDetails> findByUsername(String username);

}

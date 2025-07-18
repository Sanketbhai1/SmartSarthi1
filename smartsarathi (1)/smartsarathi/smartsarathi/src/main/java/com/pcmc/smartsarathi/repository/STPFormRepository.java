package com.pcmc.smartsarathi.repository;


import com.pcmc.smartsarathi.model.STPForm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface STPFormRepository extends JpaRepository<STPForm, Long> {
    List<STPForm> findByStatus(boolean status);

    List<STPForm> findBySocietyNameContainingIgnoreCase(String societyName);
}

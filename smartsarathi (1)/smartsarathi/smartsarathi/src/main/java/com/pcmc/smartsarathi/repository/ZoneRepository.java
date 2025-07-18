package com.pcmc.smartsarathi.repository;

import com.pcmc.smartsarathi.model.Zone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ZoneRepository extends JpaRepository<Zone, Long> {
    List<Zone> findByStatus(Boolean status);

    Zone findByTitleIgnoreCase(String title);

}

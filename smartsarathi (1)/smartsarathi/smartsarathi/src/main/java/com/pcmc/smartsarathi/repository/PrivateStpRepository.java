package com.pcmc.smartsarathi.repository;

import com.pcmc.smartsarathi.model.PrivateStp;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;

public interface PrivateStpRepository extends JpaRepository<PrivateStp, Long> {
    List<PrivateStp> findByStatus(boolean status);

    List<PrivateStp> findByZone_TitleIgnoreCase(String zoneTitle);

}

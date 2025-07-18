package com.pcmc.smartsarathi.repository;


import com.pcmc.smartsarathi.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String Username);

    @Query("SELECT u FROM User u WHERE u.id = :id AND u.status = true")
    List<User> findAllActiveUsersById(@Param("id") long id);

}

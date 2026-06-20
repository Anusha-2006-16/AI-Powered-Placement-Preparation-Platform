package com.placement_prep_platform.placement_prep_platform.repository;


import com.placement_prep_platform.placement_prep_platform.model.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Integer> {
    Optional<User> findByEmail(String email);
}

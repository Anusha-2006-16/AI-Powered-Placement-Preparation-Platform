package com.placement_prep_platform.placement_prep_platform.repository;

import com.placement_prep_platform.placement_prep_platform.model.Resume;
import com.placement_prep_platform.placement_prep_platform.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ResumeRepository extends JpaRepository<Resume, Long> {
List<Resume> findByUser(User user);
    List<Resume> findByUserOrderByUploadDateDesc(User user);
long countByUser(User user);
}

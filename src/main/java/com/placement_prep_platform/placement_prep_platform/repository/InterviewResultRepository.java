package com.placement_prep_platform.placement_prep_platform.repository;

import com.placement_prep_platform.placement_prep_platform.model.InterviewResult;
import com.placement_prep_platform.placement_prep_platform.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InterviewResultRepository extends JpaRepository<InterviewResult,Long> {
    List<InterviewResult> findByUserOrderByTakenAtDesc(User user);
}

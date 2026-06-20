package com.placement_prep_platform.placement_prep_platform.repository;

import com.placement_prep_platform.placement_prep_platform.model.TestResult;
import com.placement_prep_platform.placement_prep_platform.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TestResultRepository extends JpaRepository<TestResult, Long> {

List<TestResult> findByUser(User user);
}

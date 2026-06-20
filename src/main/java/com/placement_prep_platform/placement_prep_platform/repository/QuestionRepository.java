package com.placement_prep_platform.placement_prep_platform.repository;

import com.placement_prep_platform.placement_prep_platform.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionRepository extends JpaRepository<Question, Long> {
}

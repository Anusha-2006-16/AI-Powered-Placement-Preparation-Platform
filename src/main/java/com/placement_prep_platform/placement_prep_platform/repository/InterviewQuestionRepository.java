package com.placement_prep_platform.placement_prep_platform.repository;


import com.placement_prep_platform.placement_prep_platform.model.InterviewQuestion;
import com.placement_prep_platform.placement_prep_platform.model.Resume;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InterviewQuestionRepository extends JpaRepository<InterviewQuestion, Long> {

    List<InterviewQuestion> findByResume(Resume resume);
}

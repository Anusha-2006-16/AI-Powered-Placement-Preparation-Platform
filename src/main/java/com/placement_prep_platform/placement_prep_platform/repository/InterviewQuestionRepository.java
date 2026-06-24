package com.placement_prep_platform.placement_prep_platform.repository;


import com.placement_prep_platform.placement_prep_platform.model.InterviewQuestion;
import com.placement_prep_platform.placement_prep_platform.model.Resume;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface InterviewQuestionRepository extends JpaRepository<InterviewQuestion, Long> {

    List<InterviewQuestion> findByResume(Resume resume);
@Transactional
    @Modifying
    void deleteByResume(Resume resume);
}

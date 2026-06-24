package com.placement_prep_platform.placement_prep_platform.repository;

import com.placement_prep_platform.placement_prep_platform.model.SkillGap;
import com.placement_prep_platform.placement_prep_platform.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SkillGapRepository extends JpaRepository<SkillGap, Long> {
    List<SkillGap> findByUser(User user);
}

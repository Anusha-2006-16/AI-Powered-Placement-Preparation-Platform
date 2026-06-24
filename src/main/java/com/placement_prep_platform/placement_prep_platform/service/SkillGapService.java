package com.placement_prep_platform.placement_prep_platform.service;

import com.placement_prep_platform.placement_prep_platform.model.SkillGap;
import com.placement_prep_platform.placement_prep_platform.model.User;
import com.placement_prep_platform.placement_prep_platform.repository.SkillGapRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SkillGapService {
    private final SkillGapRepository skillGapRepository;
    public SkillGapService(SkillGapRepository skillGapRepository) {
        this.skillGapRepository = skillGapRepository;
    }
    public List<SkillGap> getSkills(User user){
        return skillGapRepository.findByUser(user);
    }
    public void save(SkillGap skillGap){
        skillGapRepository.save(skillGap);
    }
    public SkillGap findById(Long id){
        return skillGapRepository.findById(id).orElseThrow();
    }
    public long getCompletedCount(User user){
        return skillGapRepository.findByUser(user)
                .stream().filter(SkillGap::isCompleted).count();
    }
    public long getTotalCount(User user){
        return skillGapRepository.findByUser(user).size();
    }

}

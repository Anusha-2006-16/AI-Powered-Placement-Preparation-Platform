package com.placement_prep_platform.placement_prep_platform.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class SkillGap {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String skillName;
    private boolean completed=false;
    @ManyToOne
    private User user;
}

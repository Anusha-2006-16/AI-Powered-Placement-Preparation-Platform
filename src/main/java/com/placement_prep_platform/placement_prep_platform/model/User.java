package com.placement_prep_platform.placement_prep_platform.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.placement_prep_platform.placement_prep_platform.repository.Role;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "placement_prep")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
//@ToString(exclude={"resumes","testResults"})
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String email;
    private String password;
    @Enumerated(EnumType.STRING)
    private Role role;
    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private List<Resume> resumes;
    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private List<TestResult> testResults;
    @OneToMany(mappedBy = "user")
    @ToString.Exclude
    private List<SkillGap> skillGaps;
}

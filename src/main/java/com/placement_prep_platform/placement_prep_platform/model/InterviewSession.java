package com.placement_prep_platform.placement_prep_platform.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class InterviewSession {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer totalScore;

    private Integer maxScore;

    @Column(columnDefinition = "LONGTEXT")
    private String overallFeedback;

    @ManyToOne
    private User user;
}

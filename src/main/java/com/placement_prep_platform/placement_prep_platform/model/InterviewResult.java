package com.placement_prep_platform.placement_prep_platform.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "interview_result")
public class InterviewResult {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer totalScore;
    private Double percentage;

    @Column(columnDefinition = "LONGTEXT")
    private String overallFeedback;

    private LocalDateTime takenAt;
    @ManyToOne
    private User user;
}

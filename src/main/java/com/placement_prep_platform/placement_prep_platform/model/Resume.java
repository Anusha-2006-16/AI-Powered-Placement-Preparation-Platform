package com.placement_prep_platform.placement_prep_platform.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "resumes")
@ToString(exclude = {"user","questions"})
public class Resume {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String fileName;
    private String filePath;
    private LocalDateTime uploadDate;

    @Lob
    private String extractedText;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private Integer atsScore;

    @Column(length = 5000)
    private String latestFeedback;

    @Column(columnDefinition = "LONGTEXT")
    private String missingSkills;

    @OneToMany(mappedBy = "resume")
    private List<InterviewQuestion> questions;

}

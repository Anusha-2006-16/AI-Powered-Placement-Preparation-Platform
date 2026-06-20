package com.placement_prep_platform.placement_prep_platform.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"user","interviewQuestion"})
public class InterviewQuestion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "LONGTEXT")
    private String question;

    @ManyToOne
    @JoinColumn(name = "resume_id")
    private Resume resume;
}

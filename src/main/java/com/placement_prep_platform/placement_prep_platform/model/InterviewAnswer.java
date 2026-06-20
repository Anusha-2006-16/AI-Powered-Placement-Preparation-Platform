package com.placement_prep_platform.placement_prep_platform.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InterviewAnswer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(columnDefinition = "LONGTEXT")
    private String question;
    @Column(columnDefinition = "LONGTEXT")
    private String answer;

    private Integer score;

    @Column(columnDefinition = "LONGTEXT")
    private String feedback;

    @ManyToOne
    private User user;

    @ManyToOne
    private InterviewQuestion interviewQuestion;

    @ManyToOne
    private InterviewSession interviewSession;
}

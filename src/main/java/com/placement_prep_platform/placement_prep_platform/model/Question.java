package com.placement_prep_platform.placement_prep_platform.model;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "question")
@Entity
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(columnDefinition = "LONGTEXT")
    private String questionText;
    private String optionA;
    private String optionB;
    private String optionC;
    private String optionD;

    private String correctAnswer;
}

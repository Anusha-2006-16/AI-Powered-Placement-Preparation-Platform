package com.placement_prep_platform.placement_prep_platform.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "testResult")
@Entity
public class TestResult {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private int Score;

    private LocalDateTime testDate;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}

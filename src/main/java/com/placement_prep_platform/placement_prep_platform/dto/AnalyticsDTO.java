package com.placement_prep_platform.placement_prep_platform.dto;


import lombok.Data;

@Data
public class AnalyticsDTO {
    private int totalTests;
    private int highestScore;
    private double averageScore;

    private int latestScore;

    private int atsScore;
}

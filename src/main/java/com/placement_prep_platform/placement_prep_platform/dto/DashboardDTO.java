package com.placement_prep_platform.placement_prep_platform.dto;

import lombok.Data;

@Data
public class DashboardDTO {

    private long totalResumes;

    private Integer latestScore;

    private String latestFeedback;

    private String latestMissingSkills;
}

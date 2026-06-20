package com.placement_prep_platform.placement_prep_platform.dto;

import lombok.Data;

@Data
public class ResumeAnalysisResponse {
    private Integer atsScore;
    private String feedback;
    private String missingSkills;
}

package com.placement_prep_platform.placement_prep_platform.model;

import lombok.Data;

import java.util.List;

@Data
public class DashboardResponse {

    private long totalResumesUploaded;
    private Integer latestAtsScore;
    private Double averageAtsScore;

    private long mockInterviewsTaken;
    private Integer highestInterviewScore;
    private Double averageInterviewScore;

    private Double interviewReadiness;
    private List<Integer> atsScores;
    private List<Integer> interviewScores;
    public DashboardResponse(
            long totalResumesUploaded,
            Integer latestAtsScore,
            Double averageAtsScore,
            long mockInterviewsTaken,
            Integer highestInterviewScore,
            Double averageInterviewScore,
            int interviewReadiness,
            List<Integer> atsScores,
            List<Integer> interviewScores
    ) {
        this.totalResumesUploaded = totalResumesUploaded;
        this.latestAtsScore = latestAtsScore;
        this.averageAtsScore = averageAtsScore;
        this.mockInterviewsTaken = mockInterviewsTaken;
        this.highestInterviewScore = highestInterviewScore;
        this.averageInterviewScore = averageInterviewScore;
        this.interviewReadiness = (double) interviewReadiness;
        this.atsScores = atsScores;
        this.interviewScores = interviewScores;
    }
}
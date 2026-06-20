package com.placement_prep_platform.placement_prep_platform.dto;

import lombok.Data;

@Data
public class AtsResponse {
    private int score;
    private String feedback;
    public AtsResponse() {}
    public AtsResponse(int score, String feedback) {
        this.score = score;
        this.feedback = feedback;
    }


}

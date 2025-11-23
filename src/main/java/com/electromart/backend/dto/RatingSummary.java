package com.electromart.backend.dto;


public record RatingSummary(
        double avg,
        int count1,
        int count2,
        int count3,
        int count4,
        int count5
) {}

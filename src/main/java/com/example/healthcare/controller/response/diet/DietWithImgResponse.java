package com.example.healthcare.controller.response.diet;

import com.example.healthcare.controller.response.diet.DietImgUrlResponse;
import com.example.healthcare.controller.response.diet.DietInfoResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class DietWithImgResponse {
    private List<DietInfoResponse> dietResponses;
    private List<DietImgUrlResponse> dietImgUrlResponses;
}

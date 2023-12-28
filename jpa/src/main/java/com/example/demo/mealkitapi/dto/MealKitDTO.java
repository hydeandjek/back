package com.example.demo.mealkitapi.dto;

import com.example.demo.mealkitapi.entity.MealKit;
import lombok.*;

import java.util.List;

@Setter @Getter
@ToString @EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MealKitDTO {

    private int mealKitRank;
    private String mealKitImg;
    private String mealKitUrl;
    private String mealKitName;
    private String mealKitPrice;

}

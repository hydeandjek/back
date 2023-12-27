package com.example.demo.furnitureapi.dto;

import lombok.*;

@Setter @Getter
@ToString @EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FurnitureDTO {

    private int furnitureRank;
    private String furnitureImg;
    private String furnitureUrl;
    private String furnitureName;
    private String furniturePrice;

}

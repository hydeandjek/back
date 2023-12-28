package com.example.demo.applianceapi.dto;

import lombok.*;

@Setter @Getter
@ToString @EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApplianceDTO {

    private int applianceRank;
    private String applianceImg;
    private String applianceUrl;
    private String applianceName;
    private String appliancePrice;

}

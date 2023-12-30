package com.example.demo.mapapi.dto;

import lombok.*;

@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CctvResponseDTO {

    private int data_id;

    private String gu;

    private String cctvAddr;

    private String cctvName;

    private String dong;

    private String cctvNum;

    private String x;
    private String y;

}

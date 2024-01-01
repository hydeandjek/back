package com.example.demo.publicapi.dto;

import lombok.*;

@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PublicResponseDTO {
    private int data_id;

    private String publicTitle;

    private String publicAddr;

    private String publicNum;

    private String x;
    private String y;


}

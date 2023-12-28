package com.example.demo.mapapi.dto;

import lombok.*;

import java.util.List;

@Setter
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdministrativeCodeDTO {

    private String dong;

    public void setDong(String dong) {
        this.dong = dong;
    }

}

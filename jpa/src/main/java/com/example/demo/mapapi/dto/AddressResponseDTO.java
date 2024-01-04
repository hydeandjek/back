package com.example.demo.mapapi.dto;

import lombok.*;

@Setter @Getter
@ToString @EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddressResponseDTO {

    private String userId;

    private String userName;

    private String myAddress;

}

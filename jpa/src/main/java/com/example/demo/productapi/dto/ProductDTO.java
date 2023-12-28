package com.example.demo.productapi.dto;

import lombok.*;

@Setter @Getter
@ToString @EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDTO {

    private int productRank;
    private String productImg;
    private String productUrl;
    private String productName;
    private String productPrice;

}

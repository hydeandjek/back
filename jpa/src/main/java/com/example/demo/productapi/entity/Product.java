package com.example.demo.productapi.entity;

import lombok.*;

import javax.persistence.*;

@Getter @Setter
@ToString @EqualsAndHashCode(of = "productRank")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Product {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_rank", nullable = false)
    private int productRank;
    
    @Column(name = "image")
    private String productImg; // 생필품 사진
    
    @Column(name = "link")
    private String productUrl; // 생필품 링크

    @Column(name = "name")
    private String productName; // 생필품 이름

    @Column(name = "price")
    private String productPrice;// 생필품 가격
    
}

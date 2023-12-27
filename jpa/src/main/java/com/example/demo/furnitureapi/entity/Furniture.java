package com.example.demo.furnitureapi.entity;

import lombok.*;

import javax.persistence.*;

@Getter @Setter
@ToString @EqualsAndHashCode(of = "furnitureRank")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Furniture {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "furniture_rank", nullable = false)
    private int furnitureRank;

    @Column(name = "image")
    private String furnitureImg; // 가구 사진

    @Column(name = "link")
    private String furnitureUrl; // 가구 링크

    @Column(name = "name")
    private String furnitureName; // 가구 이름

    @Column(name = "price")
    private String furniturePrice;// 가구 가격

}

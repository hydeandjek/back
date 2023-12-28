package com.example.demo.applianceapi.entity;

import lombok.*;

import javax.persistence.*;

@Getter @Setter
@ToString @EqualsAndHashCode(of = "applianceRank")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Appliance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "appliance_rank", nullable = false)
    private int applianceRank;

    @Column(name = "image")
    private String applianceImg; // 가전 제품 사진

    @Column(name = "link")
    private String applianceUrl; // 가전 제품 링크

    @Column(name = "name")
    private String applianceName; // 가전 제품 이름

    @Column(name = "price")
    private String appliancePrice;// 가전 제품 가격

}

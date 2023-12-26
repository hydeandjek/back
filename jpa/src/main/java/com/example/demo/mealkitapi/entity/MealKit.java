package com.example.demo.mealkitapi.entity;

import lombok.*;

import javax.persistence.*;

@Getter @Setter
@ToString @EqualsAndHashCode(of = "mealKitRank")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class MealKit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "meal_kit_rank", nullable = false)
    private int mealKitRank;

    @Column(name = "image")
    private String mealKitImg; // 밀키트 사진

    @Column(name = "link")
    private String mealKitUrl; // 밀키트 링크

    @Column(name = "name")
    private String mealKitName; // 밀키트 이름

    @Column(name = "price")
    private String mealKitPrice; // 밀키트 가격

}

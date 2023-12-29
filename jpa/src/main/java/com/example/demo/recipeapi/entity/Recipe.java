package com.example.demo.recipeapi.entity;

import com.example.demo.userapi.entity.User;
import lombok.*;

import javax.persistence.*;

@Getter @Setter
@ToString @EqualsAndHashCode(of = "recipeId")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Recipe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int recipeId;

    private String name;

    private String way; // 조리방법

    private String type; // 요리종류

    private String imgSrc;

    private boolean done; // 좋아요 여부

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;



}

package com.example.demo.publicapi.entity;

import lombok.*;

import javax.persistence.*;

@Getter @Setter
@ToString @EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Public {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "data_id" , nullable = false)
    private int data_id;

    @Column(name = "title")
    private String publicTitle;

    @Column(name = "addr")
    private String publicAddr;

    @Column(name = "num")
    private String publicNum;

    @Column
    private String x;

    @Column
    private String y;
}

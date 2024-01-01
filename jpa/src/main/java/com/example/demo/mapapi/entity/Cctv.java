package com.example.demo.mapapi.entity;

import lombok.*;

import javax.persistence.*;

@Getter @Setter
@ToString @EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Cctv {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "data_id" , nullable = false)
    private int data_id;

    private String gu;

    @Column(name = "address")
    private String cctvAddr;

    private String dong;

    @Column(name = "name")
    private String cctvName;

    @Column(name = "number")
    private String cctvNum;

    @Column
    private String x;

    @Column
    private String y;

}

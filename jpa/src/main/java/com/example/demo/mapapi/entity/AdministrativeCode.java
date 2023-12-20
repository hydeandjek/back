package com.example.demo.mapapi.entity;

import lombok.*;

import javax.persistence.*;

@Getter @Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "administrative_code")
public class AdministrativeCode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int code;

    @Column(name = "gu")
    private String gu;

    @Column(name = "dong")
    private String dong;

}

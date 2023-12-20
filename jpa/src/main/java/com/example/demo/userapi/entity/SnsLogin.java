package com.example.demo.userapi.entity;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Setter
@Getter
@ToString
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Entity
@Table(name = "tbl_snsusers")
public class SnsLogin{

    @Id
    @Column(name = "sns_id")
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    private String id;

    @Column(nullable = false, unique = true)
    private String email; //로그인시 아이디처럼 사용


    @Enumerated(EnumType.STRING)
    private LoginType loginType;

    @Column
    private String accessToken;

}

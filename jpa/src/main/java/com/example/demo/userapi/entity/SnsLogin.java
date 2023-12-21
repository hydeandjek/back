package com.example.demo.userapi.entity;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Getter
@ToString
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Entity
@Table(name = "tbl_sns_users")
public class SnsLogin {

    @Id
    @Column(name = "sns_id")
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    private String id;


    @Setter
    @Enumerated(EnumType.STRING)
    private LoginType loginType;

    @Setter
    private String accessToken;

}

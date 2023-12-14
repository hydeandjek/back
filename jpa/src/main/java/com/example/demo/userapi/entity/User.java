package com.example.demo.userapi.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;

import javax.management.relation.Role;
import javax.persistence.*;
import java.time.LocalDateTime;

@Getter @ToString
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Entity
@Table(name = "tbl_users")
public class User {

    @Id
    @Column(name = "user_id")
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    private String id;

    @Column(nullable = false, unique = true)
    private String email; //로그인시 아이디처럼 사용

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String userName;

    private String userAddress;

    @CreationTimestamp
    private LocalDateTime joinDate;

    @OneToOne(cascade = CascadeType.ALL)
    private SnsLogin snsLogin;

    public void setSnsLogin(SnsLogin snsLogin) {this.snsLogin = snsLogin;}
}









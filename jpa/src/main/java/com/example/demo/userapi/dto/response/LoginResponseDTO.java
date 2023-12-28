package com.example.demo.userapi.dto.response;

import com.example.demo.chatapi.util.SHA256;
import com.example.demo.userapi.entity.Role;
import com.example.demo.userapi.entity.User;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDate;

@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginResponseDTO {

    private String email;
    private String userName;
    private String userId;
    private String address;

    @JsonFormat(pattern = "yyyy년 MM월 dd일")
    private LocalDate joinDate;

    private Role role; // 역할
    private String token; // 인증 토큰




    // private String message; // 로그인 메세지

    public LoginResponseDTO(User user, String token) {
        this.email = user.getEmail();
        this.userName = user.getUserName();
        this.userId = new SHA256().encrypt(user.getId());
        this.address = user.getUserAddress();
        this.joinDate = LocalDate.from(user.getJoinDate());
        this.role = user.getRole();
        this.token = token;
    }

}

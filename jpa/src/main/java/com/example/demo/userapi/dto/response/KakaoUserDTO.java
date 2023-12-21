package com.example.demo.userapi.dto.response;


import com.example.demo.userapi.entity.LoginType;
import com.example.demo.userapi.entity.SnsLogin;
import com.example.demo.userapi.entity.User;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.UUID;

@Setter
@Getter
@ToString
public class KakaoUserDTO {

    // 카카오가 주는 id
    private long id;

    @JsonProperty("connected_at")
    private LocalDateTime connectedAt;

    @JsonProperty("kakao_account")
    private KakaoAccount kakaoAccount;

    @Setter @Getter @ToString
    public static class KakaoAccount {

        private String email;
        private Profile profile;

        @Getter @Setter @ToString
        public static class Profile {
            private String nickname;

        }



    }

    public User toEntity(SnsLogin snsLogin){
        return User.builder()
                .email(this.kakaoAccount.email)
                .userName(this.kakaoAccount.profile.nickname)
                .password(UUID.randomUUID().toString())
                .userAddress("")
                .snsLogin(snsLogin)
                .build();
    }

}

package com.example.demo.userapi.dto.response;

import com.example.demo.userapi.entity.User;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Setter
@Getter
@ToString
public class NaverUserDTO {
    // 카카오가 주는 id
    private long id;

    @JsonProperty("connected_at")
    private LocalDateTime connectedAt;

    @JsonProperty("naver_account")
    private NaverUserDTO.NaverAccount NaverAccount;

    @Setter @Getter @ToString
    public static class NaverAccount {

        private String email;
        private NaverUserDTO.NaverAccount.Profile profile;

        @Getter @Setter @ToString
        public static class Profile {
            private String nickname;

            @JsonProperty("profile_image_url")
            private String profileImageUrl;
        }



    }

    public User toEntity(String accessToken){
        return User.builder()
                .email(this.NaverAccount.email)
                .userName(this.NaverAccount.profile.nickname)
                .password("password!")
                .accessToken(accessToken)
                .build();
    }
}

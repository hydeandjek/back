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
public class NaverUserDTO {


    private String resultcode;
    private String message;

    private Response response;

//    @Setter @Getter @ToString
//    public static class NaverAccount {
//
//        private String email;
//        private NaverUserDTO.NaverAccount.Profile profile;
//
//        @Getter @Setter @ToString
//        public static class Profile {
//            private String nickname;
//
//            @JsonProperty("profile_image_url")
//            private String profileImageUrl;
//        }
//
//
//
//    }

    public User toEntity(SnsLogin snsLogin){
        return User.builder()
                .email(this.response.email)
                .userName(this.response.nickname)
                .password(UUID.randomUUID().toString())
                .snsLogin(snsLogin)
                .build();
    }



    // Getters and Setters
    @Setter @Getter @ToString
    public static class Response {
        private String email;
        private String nickname;
        private String profile_image;
        private String age;
        private String gender;
        private String id;
        private String name;
        private String birthday;
}
}

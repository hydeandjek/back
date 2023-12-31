package com.example.demo.userapi.dto.request;

import com.example.demo.userapi.entity.User;
import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Setter @Getter
@ToString @EqualsAndHashCode(of = "email")
@NoArgsConstructor @AllArgsConstructor
@Builder
public class UserSignUpRequestDTO {

    @NotBlank
    @Email
    private String email;

    @NotBlank
    @Size(min = 8, max = 20)
    private String password;

    @NotBlank
    @Size(min = 2, max = 5)
    private String userName;

    private String address;


    public User toEntity(){
        return User.builder()
                .email(this.email)
                .password(this.password)
                .userName(this.userName)
                .userAddress(this.address)
                .build();
    }


}

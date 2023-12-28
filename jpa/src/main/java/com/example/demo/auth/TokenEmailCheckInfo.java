package com.example.demo.auth;

import lombok.*;

@Getter
@ToString @EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TokenEmailCheckInfo {
    private String email;
    private String authCode;
    private boolean isChecked;
}

package me.yusung.practice.spring.security.user;

import lombok.*;

/*
유저 회원가입용 DTO
 */

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class UserRegisterDto {
    private String username;
    private String password;
}

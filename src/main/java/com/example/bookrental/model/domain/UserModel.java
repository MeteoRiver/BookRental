package com.example.bookrental.model.domain;

import com.example.bookrental.model.entity.Users;
import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
//@Schema(title="사용자")
public class UserModel {
    //@Schema(title="사용자 고유ID")
    private Long id;

    //@Schema(title="사용자 ID")
    private String userName;

    //@Schema(title="비밀번호")
    private String password;

    //@Schema(title="이메일")
    private String email;

    //@Schema(title="핸드폰 번호")
    private String phone;

    public static UserModel fromEntity(Users users) {
        return UserModel.builder()
                .id(users.getUserId())
                .userName(users.getUserName())
                .password(users.getPassword())
                .email(users.getEmail())
                .phone(users.getPhone())
                .build();
    }
}

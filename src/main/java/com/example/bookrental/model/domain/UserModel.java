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
    private Long id;

    private String userName;

    private String password;

    private String email;

    private String phone;

    private String role;

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

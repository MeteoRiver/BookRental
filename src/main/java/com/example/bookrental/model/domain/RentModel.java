package com.example.bookrental.model.domain;

import com.example.bookrental.model.entity.Books;
import com.example.bookrental.model.entity.Rents;
import com.example.bookrental.model.entity.Users;
import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
//@Schema(title="렌탈")
public class RentModel {
    //@Schema(title="렌트 고유Id")
    private Long rentId;

    //@Schema(title="사용자")
    private Long userId;

    //@Schema(title="책")
    private Long bookId;

    //@Schema(title="대여여부")
    private boolean rental;  // true: 대여중, false: 재고 있음

    public static RentModel fromEntity(Rents rents) {
        return RentModel.builder()
                .rentId(rents.getRentId())
                .userId(rents.getUser().getUserId())
                .bookId(rents.getBook().getBookId())
                .build();
    }
}


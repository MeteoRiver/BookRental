package com.example.bookrental.model.domain;

import com.example.bookrental.model.entity.Books;
import com.example.bookrental.model.entity.Rents;
import com.example.bookrental.model.entity.Users;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

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

    //@Schema(title="대출일")
    private LocalDateTime RentDate;
    
    //@Schema(title=:"반납일")
    private LocalDateTime ReturnDate;

    public static RentModel fromEntity(Rents rents) {
        return RentModel.builder()
                .rentId(rents.getRentId())
                .userId(rents.getUser().getUserId())
                .bookId(rents.getBook().getBookId())
                .RentDate(rents.getRentDate())
                .ReturnDate(rents.getReturnDate())
                .build();
    }
}


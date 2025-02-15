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
public class RentModel {
    private Long rentId;

    private Long userId;

    private Long bookId;

    private LocalDateTime RentDate;
    
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


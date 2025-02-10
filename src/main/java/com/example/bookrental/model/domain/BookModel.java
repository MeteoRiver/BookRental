package com.example.bookrental.model.domain;

import com.example.bookrental.model.entity.Books;
import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
//@Schema(title="책")
public class BookModel {

    //@Schema(title="책 고유Id")
    private Long bookId;

    //@Schema(title="책 이름")
    private String bookName;

    public static BookModel fromEntity(Books books) {
        return BookModel.builder()
                .bookId(books.getBookId())
                .bookName(books.getBookName())
                .build();
    }

}

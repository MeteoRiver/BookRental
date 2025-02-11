package com.example.bookrental.model.domain;

import com.example.bookrental.model.entity.Books;
import lombok.*;

import java.time.LocalDateTime;

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

    //@Schema(title="저자")
    private String author;

    //@Schema(title="출판일")
    private LocalDateTime PubDate;

    public static BookModel fromEntity(Books books) {
        return BookModel.builder()
                .bookId(books.getBookId())
                .bookName(books.getBookName())
                .author(books.getAuthor())
                .PubDate(books.getPubDate())
                .build();
    }

}

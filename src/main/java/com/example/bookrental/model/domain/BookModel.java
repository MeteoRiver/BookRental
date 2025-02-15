package com.example.bookrental.model.domain;

import com.example.bookrental.model.entity.Books;
import lombok.*;

import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Collectors;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookModel {

    private Long bookId;
    private String bookName;
    private String author;
    private LocalDate pubDate;
    private Set<String> tagName;

    // QueryDSL에서 사용할 생성자 추가
    public BookModel(Long bookId, String bookName, String author, LocalDate pubDate) {
        this.bookId = bookId;
        this.bookName = bookName;
        this.author = author;
        this.pubDate = pubDate;
    }

    public static BookModel fromEntity(Books books) {
        return BookModel.builder()
                .bookId(books.getBookId())
                .bookName(books.getBookName())
                .author(books.getAuthor())
                .pubDate(books.getPubDate())
                .tagName(books.getTags().stream().map(tag -> tag.getName()).collect(Collectors.toSet())) // 태그 변환 추가
                .build();
    }
}

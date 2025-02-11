package com.example.bookrental.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "Book")
public class Books {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bookId;

    @Column(nullable = false,unique =true, length = 255)
    private String bookName;

    @Column(nullable = false)
    private String author;

    @Column(nullable = false)
    private LocalDateTime PubDate;
}

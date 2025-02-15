package com.example.bookrental.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    @Column(nullable = false, unique = true, length = 255)
    private String bookName;

    @Column(nullable = false)
    private String author;

    @Column(nullable = false)
    @CreatedDate
    private LocalDate pubDate;

    @PrePersist
    public void prePersist() {
        if (pubDate == null) {
            pubDate = LocalDate.now();
        }
    }

    private String tagName;

    @OneToMany(mappedBy = "book", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Rents> rents;

    @ManyToMany
    @JoinTable(
            name = "book_tag",  // 관계 테이블 이름 수정
            joinColumns = @JoinColumn(name = "book_id"),  // book 테이블의 외래 키 컬럼
            inverseJoinColumns = @JoinColumn(name = "tag_id")  // tag 테이블의 외래 키 컬럼
    )
    private Set<Tags> tags = new HashSet<>();
}
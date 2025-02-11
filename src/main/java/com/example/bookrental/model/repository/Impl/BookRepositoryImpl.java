package com.example.bookrental.model.repository.Impl;

import com.example.bookrental.model.domain.BookModel;
import com.example.bookrental.model.entity.QBooks;
import com.example.bookrental.model.repository.custom.BookRepositoryCustom;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;

@RequiredArgsConstructor
public class BookRepositoryImpl implements BookRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;
    QBooks books = QBooks.books;


    @Override
    public Page<BookModel> findAllBook(Pageable pageable) {
        var total = jpaQueryFactory
                .select(books.bookId)
                .from(books)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        List<BookModel> bookModels = total.isEmpty() ? List.of() :
                jpaQueryFactory
                        .select(Projections.constructor(
                                BookModel.class,
                                books.bookId,
                                books.bookName,
                                books.author,
                                books.PubDate
                        ))
                        .from(books)
                        .orderBy(getSort(pageable))
                        .fetch();
        return new PageImpl<>(bookModels, pageable, total.size());
    }

    // 도서 제목으로 검색 (페이징)
    @Override
    public Page<BookModel> findByName(String title, Pageable pageable) {
        var total = jpaQueryFactory
                .select(books.count())
                .from(books)
                .where(books.bookName.containsIgnoreCase(title))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        List<BookModel> bookModels = jpaQueryFactory
                .select(Projections.constructor(
                        BookModel.class,
                        books.bookId,
                        books.bookName
                ))
                .from(books)
                .where(books.bookName.containsIgnoreCase(title)) // 부분 검색 (대소문자 구분 없음)
                .orderBy(getSort(pageable))
                .fetch();

        return new PageImpl<>(bookModels, pageable, total.size());
    }

    // 저자 이름으로 검색 (페이징)
    @Override
    public Page<BookModel> findByAuthor(String author, Pageable pageable) {
        var total = jpaQueryFactory
                .select(books.count())
                .from(books)
                .where(books.author.containsIgnoreCase(author))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        List<BookModel> bookModels = jpaQueryFactory
                .select(Projections.constructor(
                        BookModel.class,
                        books.bookId,
                        books.bookName
                ))
                .from(books)
                .where(books.author.containsIgnoreCase(author)) // 저자명 부분 검색 (대소문자 무시)
                .orderBy(getSort(pageable))
                .fetch();

        return new PageImpl<>(bookModels, pageable, total.size());
    }

    // 정렬 조건 동적 적용
    private OrderSpecifier<?> getSort(Pageable pageable) {
        if (pageable.getSort().isUnsorted()) {
            return books.bookName.asc(); // 기본 정렬 (도서명 오름차순)
        }

        for (Sort.Order order : pageable.getSort()) {
            if (order.getProperty().equals("title")) {
                return order.isAscending() ? books.bookName.asc() : books.bookName.desc();
            } else if (order.getProperty().equals("publishDate")) {
                return order.isAscending() ? books.PubDate.asc() : books.PubDate.desc();
            }
        }
        return books.bookName.asc(); // 기본 정렬
    }
}

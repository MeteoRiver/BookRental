package com.example.bookrental.model.repository.Impl;

import com.example.bookrental.model.domain.BookModel;
import com.example.bookrental.model.entity.QBooks;
import com.example.bookrental.model.repository.custom.BookRepositoryCustom;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

@RequiredArgsConstructor
public class BookRepositoryImpl implements BookRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;
    QBooks books = QBooks.books;


    @Override
    public Page<BookModel> findAllBook(Pageable pageable) {
        var booksIds = jpaQueryFactory
                .select(books.bookId)
                .from(books)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        List<BookModel> bookModels = booksIds.isEmpty() ? List.of() :
                jpaQueryFactory
                        .select(Projections.constructor(
                                BookModel.class,
                                books.bookId,
                                books.bookName
                        ))
                        .from(books)
                        .where(books.bookId.in(booksIds))
                        .fetch();
        return new PageImpl<>(bookModels, pageable, bookModels.size());


    }

    @Override
    public Page<BookModel> findBookById(Long id, Pageable pageable) {
        var bookId = jpaQueryFactory
                .select(books.bookId)
                .from(books)
                .where(books.bookId.eq(id))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        List<BookModel> bookModel = bookId.isEmpty() ? List.of() :
                jpaQueryFactory
                        .select(Projections.constructor(
                                BookModel.class,
                                books.bookId,
                                books.bookName
                        ))
                        .from(books)
                        .where(books.bookId.in(bookId))
                        .fetch();


        return new PageImpl<>(bookModel, pageable, bookModel.size());
    }
}

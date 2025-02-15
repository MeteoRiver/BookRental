package com.example.bookrental.model.repository.Impl;

import com.example.bookrental.model.domain.BookModel;
import com.example.bookrental.model.entity.QBooks;
import com.example.bookrental.model.entity.QTags;
import com.example.bookrental.model.repository.custom.BookRepositoryCustom;
import com.querydsl.core.types.dsl.BooleanExpression;
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
    QTags tags = QTags.tags;

    @Override
    public Page<BookModel> findAllBook(Pageable pageable, List<String> tagNames) {
        var totalQuery = jpaQueryFactory
                .select(books.count())
                .from(books)
                .leftJoin(books.tags, tags)
                .where(filterByTags(tagNames));

        long total = totalQuery.fetchCount();

        List<BookModel> bookModels = total == 0 ? List.of() :
                jpaQueryFactory
                        .select(Projections.constructor(
                                BookModel.class,
                                books.bookId,
                                books.bookName,
                                books.author,
                                books.pubDate
                        ))
                        .from(books)
                        .leftJoin(books.tags, tags)
                        .where(filterByTags(tagNames))
                        .distinct()
                        .orderBy(getSort(pageable))
                        .offset(pageable.getOffset())
                        .limit(pageable.getPageSize())
                        .fetch();

        return new PageImpl<>(bookModels, pageable, total);
    }

    @Override
    public Page<BookModel> findByName(String bookName, Pageable pageable, List<String> tagNames) {
        var totalQuery = jpaQueryFactory
                .select(books.count())
                .from(books)
                .leftJoin(books.tags, tags)
                .where(books.bookName.containsIgnoreCase(bookName).and(filterByTags(tagNames)));

        long total = totalQuery.fetchCount();

        List<BookModel> bookModels = jpaQueryFactory
                .select(Projections.constructor(
                        BookModel.class,
                        books.bookId,
                        books.bookName,
                        books.author,
                        books.pubDate
                ))
                .from(books)
                .leftJoin(books.tags, tags)
                .where(books.bookName.containsIgnoreCase(bookName).and(filterByTags(tagNames)))
                .distinct()
                .orderBy(getSort(pageable))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return new PageImpl<>(bookModels, pageable, total);
    }

    @Override
    public Page<BookModel> findByAuthor(String author, Pageable pageable, List<String> tagNames) {
        var totalQuery = jpaQueryFactory
                .select(books.count())
                .from(books)
                .leftJoin(books.tags, tags)
                .where(books.author.containsIgnoreCase(author).and(filterByTags(tagNames)));

        long total = totalQuery.fetchCount();

        List<BookModel> bookModels = jpaQueryFactory
                .select(Projections.constructor(
                        BookModel.class,
                        books.bookId,
                        books.bookName,
                        books.author,
                        books.pubDate
                ))
                .from(books)
                .leftJoin(books.tags, tags)
                .where(books.author.containsIgnoreCase(author).and(filterByTags(tagNames)))
                .distinct()
                .orderBy(getSort(pageable))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return new PageImpl<>(bookModels, pageable, total);
    }

    private BooleanExpression filterByTags(List<String> tagNames) {
        if (tagNames == null || tagNames.isEmpty()) {
            return null;
        }
        return tags.name.in(tagNames);
    }

    private OrderSpecifier<?> getSort(Pageable pageable) {
        if (pageable.getSort().isUnsorted()) {
            return books.bookName.asc();
        }

        for (Sort.Order order : pageable.getSort()) {
            switch (order.getProperty()) {
                case "bookName":
                    return order.isAscending() ? books.bookName.asc() : books.bookName.desc();
                case "pubDate":
                    return order.isAscending() ? books.pubDate.asc() : books.pubDate.desc();
            }
        }
        return books.bookName.asc();
    }
}
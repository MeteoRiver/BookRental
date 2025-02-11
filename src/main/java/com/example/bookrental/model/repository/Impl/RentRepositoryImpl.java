package com.example.bookrental.model.repository.Impl;

import com.example.bookrental.model.entity.QRents;
import com.example.bookrental.model.repository.RentRepository;
import com.example.bookrental.model.repository.custom.RentRepositoryCustom;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

public class RentRepositoryImpl implements RentRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;
    private final QRents rents = QRents.rents;

    public RentRepositoryImpl(JPAQueryFactory jpaQueryFactory) {
        this.jpaQueryFactory = jpaQueryFactory;
    }

}

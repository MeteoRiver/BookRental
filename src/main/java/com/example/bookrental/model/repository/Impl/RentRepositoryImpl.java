package com.example.bookrental.model.repository.Impl;

import com.example.bookrental.model.entity.QRents;
import com.example.bookrental.model.repository.RentRepository;
import com.example.bookrental.model.repository.custom.RentRepositoryCustom;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class RentRepositoryImpl implements RentRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;
    private final QRents rents = QRents.rents;

    public RentRepositoryImpl(JPAQueryFactory jpaQueryFactory) {
        this.jpaQueryFactory = jpaQueryFactory;
    }

    public Boolean stateByRentId(Long rentId) {
        Boolean rentalState = jpaQueryFactory
                .select(rents.rental) // rental 컬럼 조회
                .from(rents)
                .where(rents.rentId.eq(rentId))
                .fetchOne(); // 단일 값 조회

        return rentalState != null ? rentalState : false; // 기본값 false
    }
}

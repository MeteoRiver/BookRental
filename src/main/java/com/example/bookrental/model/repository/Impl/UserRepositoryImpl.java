package com.example.bookrental.model.repository.Impl;

import com.example.bookrental.model.domain.UserModel;
import com.example.bookrental.model.entity.QUsers;
import com.example.bookrental.model.repository.custom.UserRepositoryCustom;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;
    QUsers user = QUsers.users;

    @Override
    public Page<UserModel> findAll(Pageable pageable) {
        var users = jpaQueryFactory.selectFrom(user)
                .select(user.userId)
                .from(user)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        List<UserModel> userModels = users.isEmpty() ? List.of() :
                jpaQueryFactory
                        .select(Projections.constructor(
                                UserModel.class,
                                user.userId,
                                user.userName,
                                user.email,
                                user.phone
                        ))
                        .from(user)
                        .where(user.userId.in(users))
                        .fetch();
        return new PageImpl<>(userModels, pageable, users.size());
    }
}

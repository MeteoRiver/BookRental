package com.example.bookrental.service;

import com.example.bookrental.model.domain.RentModel;

public interface RentService {

    //대출 등록
    Object insert(Long bookId, Long userId);

    //도서 대출
    boolean rent(Long id, Long userId);

    //도서 반납
    boolean remove(Long id);

    //대출 상태 확인
    boolean rentState(Long id);

    //대출 상태 확인(책번호)
    boolean bookState(Long bookId);
    }

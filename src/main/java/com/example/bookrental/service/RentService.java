package com.example.bookrental.service;

import com.example.bookrental.model.domain.RentModel;

public interface RentService {
    //도서 대출
    Object rent(RentModel rent);

    //도서 반납
    boolean remove(Long id);

    //대출 상태 확인
    boolean rentState(Long id);

}

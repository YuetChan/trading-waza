package com.tycorp.tw.repository;

import com.tycorp.tw.domain.Ticker;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Set;

public interface ComplexTickerRepository {

    Page<Ticker> findByNameLike(String name);
    Set<Ticker> findAllByNamesOrCreate(Set<String> names);

}

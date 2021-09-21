package com.tycorp.tw.repository;

import com.tycorp.tw.domain.Ticker;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Set;

public interface ComplexTickerRepository {

    Page<Ticker> findByMasterIdAndNameLike(Long masterId, String name, Pageable pageable);
    Set<Ticker> findAllByNamesOrCreate(Set<String> names);

}

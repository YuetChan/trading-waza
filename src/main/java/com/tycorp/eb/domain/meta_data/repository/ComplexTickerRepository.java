package com.tycorp.eb.domain.meta_data.repository;

import com.tycorp.eb.domain.meta_data.model.Ticker;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ComplexTickerRepository {

    public Page<Ticker> findByMasterIdAndNameLike(Long masterId, String name, Pageable pageable);

}

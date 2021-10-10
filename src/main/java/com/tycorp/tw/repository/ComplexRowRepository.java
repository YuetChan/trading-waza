package com.tycorp.tw.repository;

import com.tycorp.tw.domain.Row;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface ComplexRowRepository {

    Page<Row> findByFilter(Long processedAt, Long masterId, Set<String> tickers, Set<String> tags,
                           Pageable pageable);

}

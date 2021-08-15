package com.tycorp.eb.domain.thread.repository;

import com.tycorp.eb.domain.thread.model.Thread;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ComplexThreadRepository {

    Page<Thread> findByFilter(Long processedAt, Long masterId,
                              List<String> tickers, List<String> tags,
                              Pageable pageable);

}

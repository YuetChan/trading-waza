package com.tycorp.tb.repository;

import com.tycorp.tb.domain.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Set;

public interface ComplexPostRepository {

    Page<Post> findByFilter(Long processedAt, Long masterId, Set<String> tickers, Set<String> tags, Pageable pageable);

}

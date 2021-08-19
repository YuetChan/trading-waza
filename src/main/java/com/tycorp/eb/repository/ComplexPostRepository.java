package com.tycorp.eb.repository;

import com.tycorp.eb.domain.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ComplexPostRepository {

    Page<Post> findByFilter(Long processedAt, Long masterId,
                            List<String> tickers, List<String> tags,
                            Pageable pageable);

}

package com.tycorp.eb.domain.thread.repository;

import com.tycorp.eb.domain.thread.model.Thread;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ThreadRepository extends PagingAndSortingRepository<Thread, Long>, ComplexThreadRepository {

    Optional<Thread> findById(Long threadId);

}

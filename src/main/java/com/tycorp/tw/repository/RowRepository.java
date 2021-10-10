package com.tycorp.tw.repository;

import com.tycorp.tw.domain.Row;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RowRepository extends PagingAndSortingRepository<Row, Long>, ComplexRowRepository {

    Optional<Row> findById(Long rowId);

}

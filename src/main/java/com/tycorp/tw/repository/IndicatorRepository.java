package com.tycorp.tw.repository;

import com.tycorp.tw.domain.Indicator;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IndicatorRepository extends PagingAndSortingRepository<Indicator, Long>, ComplexIndicatorRepository {

    Optional<Indicator> findByName(String name);

}

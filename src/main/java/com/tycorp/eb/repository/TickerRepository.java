package com.tycorp.eb.repository;

import com.tycorp.eb.domain.Ticker;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TickerRepository extends PagingAndSortingRepository<Ticker, Long>, ComplexTickerRepository {

    Optional<Ticker> findByName(String name);


}

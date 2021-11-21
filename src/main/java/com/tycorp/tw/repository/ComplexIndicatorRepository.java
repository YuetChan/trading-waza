package com.tycorp.tw.repository;

import com.tycorp.tw.domain.Indicator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Set;

public interface ComplexIndicatorRepository {

    Page<Indicator> findByNameLike(String name);
    Set<Indicator> findAllByNamesOrCreate(Set<String> names);

}

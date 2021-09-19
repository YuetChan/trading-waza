package com.tycorp.eb.repository;

import com.tycorp.eb.domain.Tag;
import com.tycorp.eb.domain.Ticker;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Set;

public interface ComplexTagRepository {

    Page<Tag> findByMasterIdAndNameLike(Long masterId, String name, Pageable pageable);
    Set<Tag> findAllByNamesOrCreate(Set<String> names);

}

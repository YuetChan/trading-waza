package com.tycorp.tw.repository;

import com.tycorp.tw.domain.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Set;

public interface ComplexTagRepository {

    Page<Tag> findByMasterIdAndNameLike(Long masterId, String name, Pageable pageable);
    Set<Tag> findAllByNamesOrCreate(Set<String> names);

}

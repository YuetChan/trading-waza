package com.tycorp.eb.domain.meta_data.repository;

import com.tycorp.eb.domain.meta_data.model.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ComplexTagRepository {

    Page<Tag> findByMasterIdAndNameLike(Long masterId, String name, Pageable pageable);

}

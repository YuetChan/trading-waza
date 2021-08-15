package com.tycorp.eb.domain.meta_data.repository;

import com.tycorp.eb.domain.meta_data.model.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TagRepository extends PagingAndSortingRepository<Tag, Long>, ComplexTagRepository {

    Optional<Tag> findByName(String name);

}

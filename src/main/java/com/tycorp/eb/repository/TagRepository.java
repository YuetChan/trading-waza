package com.tycorp.eb.repository;

import com.tycorp.eb.domain.Tag;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TagRepository extends PagingAndSortingRepository<Tag, Long>, ComplexTagRepository {

    Optional<Tag> findByName(String name);

}

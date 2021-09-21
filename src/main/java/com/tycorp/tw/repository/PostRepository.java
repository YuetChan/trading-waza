package com.tycorp.tw.repository;

import com.tycorp.tw.domain.Post;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PostRepository extends PagingAndSortingRepository<Post, Long>, ComplexPostRepository {

    Optional<Post> findById(Long postId);

}

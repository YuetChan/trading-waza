package com.tycorp.eb.domain.user.repository;

import com.tycorp.eb.domain.user.model.EbUser;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EbUserRepository extends CrudRepository<EbUser, Long> {

    Optional<EbUser> findById(Long userId);
    Optional<EbUser> findByUseremail(String useremail);

}

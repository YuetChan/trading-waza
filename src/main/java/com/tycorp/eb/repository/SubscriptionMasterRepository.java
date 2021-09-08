package com.tycorp.eb.repository;

import com.tycorp.eb.domain.SubscriptionMaster;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubscriptionMasterRepository extends PagingAndSortingRepository<SubscriptionMaster, Long>, ComplexSubscriptionMasterRepository { }

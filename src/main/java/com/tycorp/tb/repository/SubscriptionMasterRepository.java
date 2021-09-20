package com.tycorp.tb.repository;

import com.tycorp.tb.domain.SubscriptionMaster;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubscriptionMasterRepository extends PagingAndSortingRepository<SubscriptionMaster, Long>, ComplexSubscriptionMasterRepository { }

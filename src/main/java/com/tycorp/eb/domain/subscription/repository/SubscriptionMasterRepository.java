package com.tycorp.eb.domain.subscription.repository;

import com.tycorp.eb.domain.subscription.model.SubscriptionMaster;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubscriptionMasterRepository extends PagingAndSortingRepository<SubscriptionMaster, Long>, ComplexSubscriptionMasterRepository { }

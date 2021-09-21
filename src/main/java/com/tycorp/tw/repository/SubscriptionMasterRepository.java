package com.tycorp.tw.repository;

import com.tycorp.tw.domain.SubscriptionMaster;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubscriptionMasterRepository extends PagingAndSortingRepository<SubscriptionMaster, Long>, ComplexSubscriptionMasterRepository { }

package com.tycorp.tb.repository;

import com.tycorp.tb.domain.SubscriptionSlave;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubscriptionSlaveRepository extends PagingAndSortingRepository<SubscriptionSlave, Long> , ComplexSubscriptionSlaveRepository{ }

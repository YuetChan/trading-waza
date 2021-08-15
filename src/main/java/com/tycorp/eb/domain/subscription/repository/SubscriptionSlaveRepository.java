package com.tycorp.eb.domain.subscription.repository;

import com.tycorp.eb.domain.subscription.model.SubscriptionSlave;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubscriptionSlaveRepository extends PagingAndSortingRepository<SubscriptionSlave, Long> , ComplexSubscriptionSlaveRepository{ }

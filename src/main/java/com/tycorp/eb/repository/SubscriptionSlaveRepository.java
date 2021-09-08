package com.tycorp.eb.repository;

import com.tycorp.eb.domain.SubscriptionSlave;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubscriptionSlaveRepository extends PagingAndSortingRepository<SubscriptionSlave, Long> , ComplexSubscriptionSlaveRepository{ }

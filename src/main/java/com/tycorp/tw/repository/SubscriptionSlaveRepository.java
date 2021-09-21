package com.tycorp.tw.repository;

import com.tycorp.tw.domain.SubscriptionSlave;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubscriptionSlaveRepository extends PagingAndSortingRepository<SubscriptionSlave, Long> , ComplexSubscriptionSlaveRepository{ }

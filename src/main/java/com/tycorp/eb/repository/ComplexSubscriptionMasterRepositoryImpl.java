package com.tycorp.eb.repository;

import com.tycorp.eb.domain.SubscriptionMaster;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class ComplexSubscriptionMasterRepositoryImpl implements ComplexSubscriptionMasterRepository {

    @Autowired
    private SubscriptionMasterRepository masterRepo;

    @Override
    public Set<SubscriptionMaster> getDefaultSubscriptionMasters() {
        return new HashSet(Arrays.asList(masterRepo.findById(1l).get()));
    }

}
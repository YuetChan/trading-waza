package com.tycorp.tw.repository;

import com.tycorp.tw.domain.SubscriptionMaster;
import com.tycorp.tw.domain.SubscriptionSlave;

public interface ComplexSubscriptionSlaveRepository {

    SubscriptionSlave getDefaultSubscriptionSlave(SubscriptionMaster master);

}

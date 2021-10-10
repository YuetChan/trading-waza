package com.tycorp.tw.repository;

import com.tycorp.tw.domain.Indicator;
import com.tycorp.tw.domain.SubscriptionMaster_;
import com.tycorp.tw.domain.SubscriptionMaster;
import com.tycorp.tw.domain.Indicator_;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.*;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ComplexIndicatorRepositoryImpl implements ComplexIndicatorRepository {

    @Autowired
    private IndicatorRepository indicatorRepo;

    @PersistenceContext
    protected EntityManager em;
    
    @Override
    public Page<Indicator> findByMasterIdAndNameLike(Long masterId, String name, Pageable pageable) {
        CriteriaBuilder cBuilder = em.getCriteriaBuilder();

        CriteriaQuery cqIndicator = cBuilder.createQuery(Indicator.class);
        Root<Indicator> rIndicator = cqIndicator.from(Indicator.class);


        SetJoin indicator_masters_join = rIndicator.join(Indicator_.masters, JoinType.LEFT);
        Predicate matchMaster = masterId == null ? cBuilder.conjunction() : cBuilder.equal(indicator_masters_join.get(SubscriptionMaster_.masterId), masterId);

        Predicate matchPattern = name == null ? cBuilder.conjunction() : cBuilder.like(rIndicator.get(Indicator_.name), name);
        Predicate matchAll = cBuilder.and(matchMaster, matchPattern);

        List<Indicator> matchedIndicators = em.createQuery(cqIndicator.select(rIndicator).where(matchAll).distinct(true))
                .setFirstResult(pageable.getPageNumber()).setMaxResults(pageable.getPageSize())
                .getResultList();


        CriteriaQuery<Long> cqLong = cBuilder.createQuery(Long.class);
        Root<Indicator> rIndicator_count = cqLong.from(Indicator.class);

        SetJoin<Indicator, SubscriptionMaster> indicator_masters_join_count = rIndicator_count.join(Indicator_.masters, JoinType.LEFT);
        Predicate matchMaster_count = masterId == null?
                cBuilder.conjunction() : cBuilder.equal(indicator_masters_join_count.get(SubscriptionMaster_.masterId), masterId);

        Predicate matchPattern_count = name == null ? cBuilder.conjunction() : cBuilder.like(rIndicator_count.get(Indicator_.name), name);
        Predicate matchAll_count = cBuilder.and(matchMaster_count, matchPattern_count);

        Long matchedCount = em.createQuery(cqLong.select(cBuilder.count(rIndicator_count)).where(matchAll_count).distinct(true))
                .getSingleResult();


        return new PageImpl(matchedIndicators, pageable, matchedCount);
    }

    @Override
    public Set<Indicator> findAllByNamesOrCreate(Set<String> names) {
        return names.stream().map(name -> indicatorRepo.findByName(name).orElseGet(() -> indicatorRepo.save(new Indicator(name))))
                .collect(Collectors.toSet());
    }

}

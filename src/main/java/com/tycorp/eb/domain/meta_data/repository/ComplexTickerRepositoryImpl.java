package com.tycorp.eb.domain.meta_data.repository;

import com.tycorp.eb.domain.meta_data.model.Ticker;
import com.tycorp.eb.domain.meta_data.model.Ticker_;
import com.tycorp.eb.domain.meta_data.model.Ticker;
import com.tycorp.eb.domain.subscription.model.SubscriptionMaster_;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.JoinType;

public class ComplexTickerRepositoryImpl implements  ComplexTickerRepository {

    @PersistenceContext
    protected EntityManager em;
    
    @Override
    public Page<Ticker> findByMasterIdAndNameLike(Long masterId, String name, Pageable pageable) {
        var cBuilder = em.getCriteriaBuilder();

        var cqTicker = cBuilder.createQuery(Ticker.class);
        var rTicker = cqTicker.from(Ticker.class);


        var ticker_masters_join = rTicker.join(Ticker_.masters, JoinType.LEFT);
        var matchMaster = masterId == null?
                cBuilder.conjunction() : cBuilder.equal(ticker_masters_join.get(SubscriptionMaster_.masterId), masterId);

        var matchPattern = name == null ? cBuilder.conjunction() : cBuilder.like(rTicker.get(Ticker_.name), name);


        var matchAll = cBuilder.and(matchMaster, matchPattern);

        var matchedTickers = em.createQuery(cqTicker.select(rTicker).where(matchAll).distinct(true))
                .setFirstResult(pageable.getPageNumber())
                .setMaxResults(pageable.getPageSize())
                .getResultList();


        var cqLong = cBuilder.createQuery(Long.class);
        var rTicker_count = cqLong.from(Ticker.class);


        var ticker_masters_join_count = rTicker_count.join(Ticker_.masters, JoinType.LEFT);
        var matchMaster_count = masterId == null?
                cBuilder.conjunction() : cBuilder.equal(ticker_masters_join_count.get(SubscriptionMaster_.masterId), masterId);

        var matchPattern_count = name == null ? cBuilder.conjunction() : cBuilder.like(rTicker_count.get(Ticker_.name), name);


        var matchAll_count = cBuilder.and(matchMaster_count, matchPattern_count);

        var matchedCount = em.createQuery(
                cqLong.select(cBuilder.count(rTicker_count)).where(matchAll_count).distinct(true)).getSingleResult();

        return new PageImpl(matchedTickers, pageable, matchedCount);
    }

}

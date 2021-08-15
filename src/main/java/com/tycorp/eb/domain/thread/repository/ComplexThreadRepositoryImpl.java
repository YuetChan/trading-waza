package com.tycorp.eb.domain.thread.repository;

import com.tycorp.eb.domain.meta_data.model.Tag_;
import com.tycorp.eb.domain.meta_data.model.Ticker_;
import com.tycorp.eb.domain.subscription.model.SubscriptionMaster_;
import com.tycorp.eb.domain.thread.model.*;
import com.tycorp.eb.domain.thread.model.Thread;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.*;
import java.util.List;

public class ComplexThreadRepositoryImpl implements ComplexThreadRepository {

    @PersistenceContext
    protected EntityManager em;

    @Override
    public Page<Thread> findByFilter(Long processedAt, Long masterId,
                                     List<String> tickers, List<String> tags,
                                     Pageable pageable) {
        var cBuilder = em.getCriteriaBuilder();

        var cqThread = cBuilder.createQuery(Thread.class);
        var rThread = cqThread.from(Thread.class);


        var thread_masters_join = rThread.join(Thread_.master, JoinType.LEFT);
        var matchMaster = masterId == null?
                cBuilder.conjunction() : cBuilder.equal(thread_masters_join.get(SubscriptionMaster_.masterId), masterId);


        var threads_tickers_join = rThread.join(Thread_.tickers, JoinType.LEFT);
        var matchTickers = tickers == null ?
                cBuilder.conjunction()
                : cBuilder.and(
                        tickers.stream().map(ticker -> cBuilder.equal(threads_tickers_join.get(Ticker_.name), ticker))
                                 .toArray(Predicate[]::new));

        var threads_tags_join = rThread.join(Thread_.tags, JoinType.LEFT);
        var matchTags = tags == null ?
                cBuilder.conjunction()
                : cBuilder.and(
                        tags.stream().map(tag -> cBuilder.equal(threads_tags_join.get(Tag_.name), tag))
                              .toArray(Predicate[]::new));

        var matchProcessedAt = cBuilder.greaterThanOrEqualTo(rThread.get(Thread_.processedAt), processedAt);


        var matchAll = cBuilder.and(
                matchMaster,
                matchTickers, matchTags,
                matchProcessedAt);

        var matchedThreads = em.createQuery(cqThread.select(rThread).where(matchAll).distinct(true))
                .setFirstResult(pageable.getPageNumber())
                .setMaxResults(pageable.getPageSize())
                .getResultList();


        CriteriaQuery<Long> cqLong = cBuilder.createQuery(Long.class);
        Root<Thread> rThread_count = cqLong.from(Thread.class);


        var threads_master_join_count = rThread_count.join(Thread_.master, JoinType.LEFT);
        var matchMaster_count = masterId == null ?
                cBuilder.conjunction() : cBuilder.equal(threads_master_join_count.get(SubscriptionMaster_.masterId), masterId);


        var threads_tickers_join_count = rThread_count.join(Thread_.tickers, JoinType.LEFT);
        var matchTickers_count = tickers == null ?
                cBuilder.conjunction()
                : cBuilder.and(
                        tickers.stream().map(ticker -> cBuilder.equal(threads_tickers_join_count.get(Ticker_.name), ticker))
                                 .toArray(Predicate[]::new));

        var threads_tags_join_count = rThread_count.join(Thread_.tags, JoinType.LEFT);
        var matchTags_count = tags == null ?
                cBuilder.conjunction()
                : cBuilder.and(
                        tags.stream().map(tag -> cBuilder.equal(threads_tags_join_count.get(Tag_.name), tag))
                              .toArray(Predicate[]::new));

        var matchProcessedAt_count = cBuilder.greaterThanOrEqualTo(rThread_count.get(Thread_.processedAt), processedAt);


        var matchAll_count = cBuilder.and(
                matchMaster_count,
                matchTickers_count, matchTags_count,
                matchProcessedAt_count);


        var matchedCount = em.createQuery(
                cqLong.select(cBuilder.count(rThread_count)).where(matchAll_count).distinct(true)).getSingleResult();


        return new PageImpl(matchedThreads, pageable, matchedCount);

    }
}

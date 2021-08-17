package com.tycorp.eb.domain.thread.repository;

import com.tycorp.eb.domain.meta_data.model.Tag;
import com.tycorp.eb.domain.meta_data.model.Tag_;
import com.tycorp.eb.domain.meta_data.model.Ticker;
import com.tycorp.eb.domain.meta_data.model.Ticker_;
import com.tycorp.eb.domain.subscription.model.SubscriptionMaster;
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
        CriteriaBuilder cBuilder = em.getCriteriaBuilder();

        CriteriaQuery<Thread> cqThread = cBuilder.createQuery(Thread.class);
        Root<Thread> rThread = cqThread.from(Thread.class);


        Join<Thread, SubscriptionMaster> thread_masters_join = rThread.join(Thread_.master, JoinType.LEFT);
        Predicate matchMaster = masterId == null?
                cBuilder.conjunction() : cBuilder.equal(thread_masters_join.get(SubscriptionMaster_.masterId), masterId);


        SetJoin<Thread, Ticker> threads_tickers_join = rThread.join(Thread_.tickers, JoinType.LEFT);
        Predicate matchTickers = tickers == null ?
                cBuilder.conjunction()
                : cBuilder.and(
                        tickers.stream().map(ticker -> cBuilder.equal(threads_tickers_join.get(Ticker_.name), ticker))
                                 .toArray(Predicate[]::new));

        SetJoin<Thread, Tag> threads_tags_join = rThread.join(Thread_.tags, JoinType.LEFT);
        Predicate matchTags = tags == null ?
                cBuilder.conjunction()
                : cBuilder.and(
                        tags.stream().map(tag -> cBuilder.equal(threads_tags_join.get(Tag_.name), tag))
                              .toArray(Predicate[]::new));

        Predicate matchProcessedAt = cBuilder.greaterThanOrEqualTo(rThread.get(Thread_.processedAt), processedAt);


        Predicate matchAll = cBuilder.and(
                matchMaster,
                matchTickers, matchTags,
                matchProcessedAt);

        List<Thread> matchedThreads = em.createQuery(cqThread.select(rThread).where(matchAll).distinct(true))
                .setFirstResult(pageable.getPageNumber())
                .setMaxResults(pageable.getPageSize())
                .getResultList();


        CriteriaQuery<Long> cqLong = cBuilder.createQuery(Long.class);
        Root<Thread> rThread_count = cqLong.from(Thread.class);


        Join<Thread, SubscriptionMaster> threads_master_join_count = rThread_count.join(Thread_.master, JoinType.LEFT);
        Predicate matchMaster_count = masterId == null ?
                cBuilder.conjunction() : cBuilder.equal(threads_master_join_count.get(SubscriptionMaster_.masterId), masterId);


        SetJoin<Thread, Ticker> threads_tickers_join_count = rThread_count.join(Thread_.tickers, JoinType.LEFT);
        Predicate matchTickers_count = tickers == null ?
                cBuilder.conjunction()
                : cBuilder.and(
                        tickers.stream().map(ticker -> cBuilder.equal(threads_tickers_join_count.get(Ticker_.name), ticker))
                                 .toArray(Predicate[]::new));

        SetJoin<Thread, Tag> threads_tags_join_count = rThread_count.join(Thread_.tags, JoinType.LEFT);
        Predicate matchTags_count = tags == null ?
                cBuilder.conjunction()
                : cBuilder.and(
                        tags.stream().map(tag -> cBuilder.equal(threads_tags_join_count.get(Tag_.name), tag))
                              .toArray(Predicate[]::new));

        Predicate matchProcessedAt_count = cBuilder.greaterThanOrEqualTo(rThread_count.get(Thread_.processedAt), processedAt);


        Predicate matchAll_count = cBuilder.and(
                matchMaster_count,
                matchTickers_count, matchTags_count,
                matchProcessedAt_count);


        Long matchedCount = em.createQuery(
                cqLong.select(cBuilder.count(rThread_count)).where(matchAll_count).distinct(true)).getSingleResult();


        return new PageImpl(matchedThreads, pageable, matchedCount);

    }
}

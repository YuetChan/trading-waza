package com.tycorp.tw.repository;

import com.tycorp.tw.domain.SubscriptionMaster_;
import com.tycorp.tw.domain.Row_;
import com.tycorp.tw.domain.Indicator_;
import com.tycorp.tw.domain.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.*;
import java.util.List;
import java.util.Set;

public class ComplexRowRepositoryImpl implements ComplexRowRepository {

    @PersistenceContext
    protected EntityManager em;

    @Override
    public Page<Row> findByFilter(Long processedAt, Long masterId, Set<String> indicators, Pageable pageable) {
        CriteriaBuilder cBuilder = em.getCriteriaBuilder();

        CriteriaQuery<Row> cqRow = cBuilder.createQuery(Row.class);
        Root<Row> rRow = cqRow.from(Row.class);


        Join<Row, SubscriptionMaster> row_masters_join = rRow.join(Row_.master, JoinType.LEFT);
        Predicate matchMaster = masterId == null?
                cBuilder.conjunction() : cBuilder.equal(row_masters_join.get(SubscriptionMaster_.masterId), masterId);

        SetJoin<Row, Indicator> rows_indicators_join = rRow.join(Row_.indicators, JoinType.LEFT);
        Predicate matchIndicators = indicators == null ?
                cBuilder.conjunction() : cBuilder.and(indicators.stream().map(indicator -> cBuilder.equal(rows_indicators_join.get(Indicator_.name), indicator))
                .toArray(Predicate[]::new));

        Predicate matchProcessedAt = cBuilder.equal(rRow.get(Row_.processedAt), processedAt);
        Predicate matchAll = cBuilder.and(matchMaster, matchIndicators, matchProcessedAt);

        rRow.fetch(Row_.indicators, JoinType.LEFT);
        rRow.fetch(Row_.ticker, JoinType.LEFT);

        List<Row> matchedRows = em.createQuery(cqRow.select(rRow).where(matchAll).distinct(true))
                .setFirstResult(pageable.getPageNumber())
                .setMaxResults(pageable.getPageSize())
                .getResultList();


        CriteriaQuery<Long> cqLong = cBuilder.createQuery(Long.class);
        Root<Row> rRow_count = cqLong.from(Row.class);

        Join<Row, SubscriptionMaster> rows_master_join_count = rRow_count.join(Row_.master, JoinType.LEFT);
        Predicate matchMaster_count = masterId == null ?
                cBuilder.conjunction() : cBuilder.equal(rows_master_join_count.get(SubscriptionMaster_.masterId), masterId);

        SetJoin<Row, Indicator> rows_indicators_join_count = rRow_count.join(Row_.indicators, JoinType.LEFT);
        Predicate matchTags_count = indicators == null ?
                cBuilder.conjunction() : cBuilder.and(indicators.stream().map(indicator -> cBuilder.equal(rows_indicators_join_count.get(Indicator_.name), indicator))
                .toArray(Predicate[]::new));

        Predicate matchProcessedAt_count = cBuilder.equal(rRow_count.get(Row_.processedAt), processedAt);

        Predicate matchAll_count = cBuilder.and(matchMaster_count, matchTags_count, matchProcessedAt_count);
        Long matchedCount = em.createQuery(cqLong.select(cBuilder.count(rRow_count)).where(matchAll_count).distinct(true))
                .getSingleResult();


        return new PageImpl(matchedRows, pageable, matchedCount);
    }
}

package com.tycorp.tw.repository;

import com.tycorp.tw.domain.SubscriptionMaster_;
import com.tycorp.tw.domain.Row_;
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
    public Page<Row> findByFilter(Long processedAt, Long masterId, Set<String> tickers, Set<String> tags, Pageable pageable) {
        CriteriaBuilder cBuilder = em.getCriteriaBuilder();

        CriteriaQuery<Row> cqRow = cBuilder.createQuery(Row.class);
        Root<Row> rRow = cqRow.from(Row.class);


        Join<Row, SubscriptionMaster> row_masters_join = rRow.join(Row_.master, JoinType.LEFT);
        Predicate matchMaster = masterId == null?
                cBuilder.conjunction() : cBuilder.equal(row_masters_join.get(SubscriptionMaster_.masterId), masterId);

        SetJoin<Row, Ticker> rows_tickers_join = rRow.join(Row_.tickers, JoinType.LEFT);
        Predicate matchTickers = tickers == null ?
                cBuilder.conjunction() : cBuilder.and(tickers.stream().map(ticker -> cBuilder.equal(rows_tickers_join.get(Ticker_.name), ticker))
                .toArray(Predicate[]::new));

        SetJoin<Row, Tag> rows_tags_join = rRow.join(Row_.tags, JoinType.LEFT);
        Predicate matchTags = tags == null ?
                cBuilder.conjunction() : cBuilder.and(tags.stream().map(tag -> cBuilder.equal(rows_tags_join.get(Tag_.name), tag))
                .toArray(Predicate[]::new));

        Predicate matchProcessedAt = cBuilder.equal(rRow.get(Row_.processedAt), processedAt);
        Predicate matchAll = cBuilder.and(matchMaster, matchTickers, matchTags, matchProcessedAt);

        rRow.fetch(Row_.tags, JoinType.LEFT);
        rRow.fetch(Row_.tickers, JoinType.LEFT);

        List<Row> matchedRows = em.createQuery(cqRow.select(rRow).where(matchAll).distinct(true))
                .setFirstResult(pageable.getPageNumber())
                .setMaxResults(pageable.getPageSize())
                .getResultList();


        CriteriaQuery<Long> cqLong = cBuilder.createQuery(Long.class);
        Root<Row> rRow_count = cqLong.from(Row.class);

        Join<Row, SubscriptionMaster> rows_master_join_count = rRow_count.join(Row_.master, JoinType.LEFT);
        Predicate matchMaster_count = masterId == null ?
                cBuilder.conjunction() : cBuilder.equal(rows_master_join_count.get(SubscriptionMaster_.masterId), masterId);

        SetJoin<Row, Ticker> rows_tickers_join_count = rRow_count.join(Row_.tickers, JoinType.LEFT);
        Predicate matchTickers_count = tickers == null ?
                cBuilder.conjunction() : cBuilder.and(tickers.stream().map(ticker -> cBuilder.equal(rows_tickers_join_count.get(Ticker_.name), ticker))
                .toArray(Predicate[]::new));

        SetJoin<Row, Tag> rows_tags_join_count = rRow_count.join(Row_.tags, JoinType.LEFT);
        Predicate matchTags_count = tags == null ?
                cBuilder.conjunction() : cBuilder.and(tags.stream().map(tag -> cBuilder.equal(rows_tags_join_count.get(Tag_.name), tag))
                .toArray(Predicate[]::new));

        Predicate matchProcessedAt_count = cBuilder.equal(rRow_count.get(Row_.processedAt), processedAt);

        Predicate matchAll_count = cBuilder.and(matchMaster_count, matchTickers_count, matchTags_count, matchProcessedAt_count);
        Long matchedCount = em.createQuery(cqLong.select(cBuilder.count(rRow_count)).where(matchAll_count).distinct(true))
                .getSingleResult();


        return new PageImpl(matchedRows, pageable, matchedCount);
    }
}

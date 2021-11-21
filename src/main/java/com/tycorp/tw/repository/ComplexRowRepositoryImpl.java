package com.tycorp.tw.repository;

import com.tycorp.tw.domain.Row_;
import com.tycorp.tw.domain.Indicator_;
import com.tycorp.tw.domain.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.*;
import java.util.List;
import java.util.Set;

public class ComplexRowRepositoryImpl implements ComplexRowRepository {

    @PersistenceContext
    protected EntityManager em;

    @Override
    public Page<Row> findByFilter(Long processedAt, Set<String> indicators) {
        CriteriaBuilder cBuilder = em.getCriteriaBuilder();

        CriteriaQuery<Row> cqRow = cBuilder.createQuery(Row.class);
        Root rRow = cqRow.from(Row.class);

        Subquery<Long> cqSubRow = cqRow.subquery(Long.class);
        Root rSubRow = cqSubRow.from(Row.class);

        SetJoin<Row, Indicator> rows_indicators_join = rSubRow.join(Row_.indicators, JoinType.LEFT);
        Predicate matchIndicators = indicators == null ?
                cBuilder.conjunction()
                : rows_indicators_join.get(Indicator_.name).in(indicators);

        Predicate matchProcessedAt = cBuilder.equal(rSubRow.get(Row_.processedAt), processedAt);
        Predicate matchAll = cBuilder.and(matchIndicators, matchProcessedAt);

        Expression<Long> rowCount = cBuilder.count(rSubRow.get(Row_.rowId));
        Expression<String> rowId = rSubRow.get(Row_.rowId);

        cqSubRow.select(rSubRow.get(Row_.rowId)).where(matchAll).groupBy(rowId).having(cBuilder.equal(rowCount, indicators == null ? 0 : indicators.size()));
        cqRow.select(rRow).where(cBuilder.in(rRow.get(Row_.rowId)).value(cqSubRow));

        rRow.fetch(Row_.indicators, JoinType.LEFT);
        rRow.fetch(Row_.ticker, JoinType.LEFT);

        List<Row> matchedRows = em.createQuery(cqRow)
                .setFirstResult(1)
                .setMaxResults(8000)
                .getResultList();

        return new PageImpl(matchedRows);
    }
}

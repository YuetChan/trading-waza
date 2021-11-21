package com.tycorp.tw.repository;

import com.tycorp.tw.domain.Indicator;
import com.tycorp.tw.domain.Indicator_;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

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
    public Page<Indicator> findByNameLike(String name) {
        CriteriaBuilder cBuilder = em.getCriteriaBuilder();

        CriteriaQuery cqIndicator = cBuilder.createQuery(Indicator.class);
        Root<Indicator> rIndicator = cqIndicator.from(Indicator.class);

        Predicate matchPattern = name == null ? cBuilder.conjunction() : cBuilder.like(rIndicator.get(Indicator_.name), name);
        Predicate matchAll = cBuilder.and(matchPattern);

        List<Indicator> matchedIndicators = em.createQuery(cqIndicator.select(rIndicator).where(matchAll).distinct(true))
                .setFirstResult(1)
                .setMaxResults(8000)
                .getResultList();

        return new PageImpl(matchedIndicators);
    }

    @Override
    public Set<Indicator> findAllByNamesOrCreate(Set<String> names) {
        return names.stream().map(name -> indicatorRepo.findByName(name).orElseGet(() -> indicatorRepo.save(new Indicator(name))))
                .collect(Collectors.toSet());
    }

}

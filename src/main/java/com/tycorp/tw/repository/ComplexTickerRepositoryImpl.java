package com.tycorp.tw.repository;

import com.tycorp.tw.domain.Ticker;
import com.tycorp.tw.domain.Ticker_;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.*;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ComplexTickerRepositoryImpl implements  ComplexTickerRepository {

    @Autowired
    private TickerRepository tickerRepo;

    @PersistenceContext
    protected EntityManager em;
    
    @Override
    public Page<Ticker> findByNameLike(String name) {
        CriteriaBuilder cBuilder = em.getCriteriaBuilder();

        CriteriaQuery<Ticker> cqTicker = cBuilder.createQuery(Ticker.class);
        Root<Ticker> rTicker = cqTicker.from(Ticker.class);


        Predicate matchPattern = name == null ? cBuilder.conjunction() : cBuilder.like(rTicker.get(Ticker_.name), name);
        Predicate matchAll = cBuilder.and(matchPattern);

        List<Ticker> matchedTickers = em.createQuery(cqTicker.select(rTicker).where(matchAll).distinct(true))
                .setFirstResult(0)
                .setMaxResults(8000)
                .getResultList();

        return new PageImpl(matchedTickers);
    }

    @Override
    public Set<Ticker> findAllByNamesOrCreate(Set<String> names) {
        return names.stream().map(name -> tickerRepo.findByName(name).orElseGet(() -> tickerRepo.save(new Ticker(name))))
                .collect(Collectors.toSet());
    }

}

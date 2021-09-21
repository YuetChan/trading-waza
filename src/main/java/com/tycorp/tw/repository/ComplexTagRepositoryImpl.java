package com.tycorp.tw.repository;

import com.tycorp.tw.domain.SubscriptionMaster_;
import com.tycorp.tw.domain.Tag;
import com.tycorp.tw.domain.SubscriptionMaster;
import com.tycorp.tw.domain.Tag_;
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

public class ComplexTagRepositoryImpl implements  ComplexTagRepository{

    @Autowired
    private TagRepository tagRepo;

    @PersistenceContext
    protected EntityManager em;
    
    @Override
    public Page<Tag> findByMasterIdAndNameLike(Long masterId, String name, Pageable pageable) {
        CriteriaBuilder cBuilder = em.getCriteriaBuilder();

        CriteriaQuery cqTag = cBuilder.createQuery(Tag.class);
        Root<Tag> rTag = cqTag.from(Tag.class);


        SetJoin tag_masters_join = rTag.join(Tag_.masters, JoinType.LEFT);
        Predicate matchMaster = masterId == null ? cBuilder.conjunction() : cBuilder.equal(tag_masters_join.get(SubscriptionMaster_.masterId), masterId);

        Predicate matchPattern = name == null ? cBuilder.conjunction() : cBuilder.like(rTag.get(Tag_.name), name);
        Predicate matchAll = cBuilder.and(matchMaster, matchPattern);

        List<Tag> matchedTags = em.createQuery(cqTag.select(rTag).where(matchAll).distinct(true))
                .setFirstResult(pageable.getPageNumber()).setMaxResults(pageable.getPageSize())
                .getResultList();


        CriteriaQuery<Long> cqLong = cBuilder.createQuery(Long.class);
        Root<Tag> rTag_count = cqLong.from(Tag.class);

        SetJoin<Tag, SubscriptionMaster> tag_masters_join_count = rTag_count.join(Tag_.masters, JoinType.LEFT);
        Predicate matchMaster_count = masterId == null?
                cBuilder.conjunction() : cBuilder.equal(tag_masters_join_count.get(SubscriptionMaster_.masterId), masterId);

        Predicate matchPattern_count = name == null ? cBuilder.conjunction() : cBuilder.like(rTag_count.get(Tag_.name), name);
        Predicate matchAll_count = cBuilder.and(matchMaster_count, matchPattern_count);

        Long matchedCount = em.createQuery(cqLong.select(cBuilder.count(rTag_count)).where(matchAll_count).distinct(true))
                .getSingleResult();


        return new PageImpl(matchedTags, pageable, matchedCount);
    }

    @Override
    public Set<Tag> findAllByNamesOrCreate(Set<String> names) {
        return names.stream().map(name -> tagRepo.findByName(name).orElseGet(() -> tagRepo.save(new Tag(name))))
                .collect(Collectors.toSet());
    }

}

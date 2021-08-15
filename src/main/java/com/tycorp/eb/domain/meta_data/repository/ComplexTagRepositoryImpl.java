package com.tycorp.eb.domain.meta_data.repository;

import com.tycorp.eb.domain.meta_data.model.Tag;
import com.tycorp.eb.domain.meta_data.model.Tag_;
import com.tycorp.eb.domain.subscription.model.SubscriptionMaster_;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.JoinType;

public class ComplexTagRepositoryImpl implements  ComplexTagRepository{

    @PersistenceContext
    protected EntityManager em;
    
    @Override
    public Page<Tag> findByMasterIdAndNameLike(Long masterId, String name, Pageable pageable) {
        var cBuilder = em.getCriteriaBuilder();

        var cqTag = cBuilder.createQuery(Tag.class);
        var rTag = cqTag.from(Tag.class);


        var tag_masters_join = rTag.join(Tag_.masters, JoinType.LEFT);
        var matchMaster = masterId == null?
                cBuilder.conjunction() : cBuilder.equal(tag_masters_join.get(SubscriptionMaster_.masterId), masterId);

        var matchPattern = name == null ? cBuilder.conjunction() : cBuilder.like(rTag.get(Tag_.name), name);


        var matchAll = cBuilder.and(matchMaster, matchPattern);

        var matchedTags = em.createQuery(cqTag.select(rTag).where(matchAll).distinct(true))
                .setFirstResult(pageable.getPageNumber())
                .setMaxResults(pageable.getPageSize())
                .getResultList();


        var cqLong = cBuilder.createQuery(Long.class);
        var rTag_count = cqLong.from(Tag.class);


        var tag_masters_join_count = rTag_count.join(Tag_.masters, JoinType.LEFT);
        var matchMaster_count = masterId == null?
                cBuilder.conjunction() : cBuilder.equal(tag_masters_join_count.get(SubscriptionMaster_.masterId), masterId);

        var matchPattern_count = name == null ? cBuilder.conjunction() : cBuilder.like(rTag_count.get(Tag_.name), name);


        var matchAll_count = cBuilder.and(matchMaster_count, matchPattern_count);

        var matchedCount = em.createQuery(
                cqLong.select(cBuilder.count(rTag_count)).where(matchAll_count).distinct(true)).getSingleResult();

        return new PageImpl(matchedTags, pageable, matchedCount);
    }

}

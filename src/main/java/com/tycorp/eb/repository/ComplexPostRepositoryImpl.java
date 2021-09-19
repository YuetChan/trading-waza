package com.tycorp.eb.repository;

import com.tycorp.eb.domain.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.*;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Set;

public class ComplexPostRepositoryImpl implements ComplexPostRepository {

    @PersistenceContext
    protected EntityManager em;

    @Override
    public Page<Post> findByFilter(Long processedAt, Long masterId, Set<String> tickers, Set<String> tags, Pageable pageable) {
        CriteriaBuilder cBuilder = em.getCriteriaBuilder();

        CriteriaQuery<Post> cqPost = cBuilder.createQuery(Post.class);
        Root<Post> rPost = cqPost.from(Post.class);


        Join<Post, SubscriptionMaster> post_masters_join = rPost.join(Post_.master, JoinType.LEFT);
        Predicate matchMaster = masterId == null?
                cBuilder.conjunction() : cBuilder.equal(post_masters_join.get(SubscriptionMaster_.masterId), masterId);

        SetJoin<Post, Ticker> posts_tickers_join = rPost.join(Post_.tickers, JoinType.LEFT);
        Predicate matchTickers = tickers == null ?
                cBuilder.conjunction() : cBuilder.and(tickers.stream().map(ticker -> cBuilder.equal(posts_tickers_join.get(Ticker_.name), ticker))
                .toArray(Predicate[]::new));

        SetJoin<Post, Tag> posts_tags_join = rPost.join(Post_.tags, JoinType.LEFT);
        Predicate matchTags = tags == null ?
                cBuilder.conjunction() : cBuilder.and(tags.stream().map(tag -> cBuilder.equal(posts_tags_join.get(Tag_.name), tag))
                .toArray(Predicate[]::new));

        Predicate matchProcessedAt = cBuilder.equal(rPost.get(Post_.processedAt), processedAt);
        Predicate matchAll = cBuilder.and(matchMaster, matchTickers, matchTags, matchProcessedAt);

        rPost.fetch(Post_.tags, JoinType.LEFT);
        rPost.fetch(Post_.tickers, JoinType.LEFT);

        List<Post> matchedPosts = em.createQuery(cqPost.select(rPost).where(matchAll).distinct(true))
                .setFirstResult(pageable.getPageNumber())
                .setMaxResults(pageable.getPageSize())
                .getResultList();


        CriteriaQuery<Long> cqLong = cBuilder.createQuery(Long.class);
        Root<Post> rPost_count = cqLong.from(Post.class);

        Join<Post, SubscriptionMaster> posts_master_join_count = rPost_count.join(Post_.master, JoinType.LEFT);
        Predicate matchMaster_count = masterId == null ?
                cBuilder.conjunction() : cBuilder.equal(posts_master_join_count.get(SubscriptionMaster_.masterId), masterId);

        SetJoin<Post, Ticker> posts_tickers_join_count = rPost_count.join(Post_.tickers, JoinType.LEFT);
        Predicate matchTickers_count = tickers == null ?
                cBuilder.conjunction() : cBuilder.and(tickers.stream().map(ticker -> cBuilder.equal(posts_tickers_join_count.get(Ticker_.name), ticker))
                .toArray(Predicate[]::new));

        SetJoin<Post, Tag> posts_tags_join_count = rPost_count.join(Post_.tags, JoinType.LEFT);
        Predicate matchTags_count = tags == null ?
                cBuilder.conjunction() : cBuilder.and(tags.stream().map(tag -> cBuilder.equal(posts_tags_join_count.get(Tag_.name), tag))
                .toArray(Predicate[]::new));

        Predicate matchProcessedAt_count = cBuilder.equal(rPost_count.get(Post_.processedAt), processedAt);

        Predicate matchAll_count = cBuilder.and(matchMaster_count, matchTickers_count, matchTags_count, matchProcessedAt_count);
        Long matchedCount = em.createQuery(cqLong.select(cBuilder.count(rPost_count)).where(matchAll_count).distinct(true))
                .getSingleResult();


        return new PageImpl(matchedPosts, pageable, matchedCount);

    }
}

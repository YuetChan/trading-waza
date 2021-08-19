package com.tycorp.eb.domain;

import com.tycorp.eb.exception.DomainInvariantException;
import com.tycorp.eb.domain.event.PostCreatedEvent;
import lombok.*;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "post")
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PRIVATE)
@Where(clause = "is_active=1")
public class Post extends AbstractDomainEntityTemplate {

    @Transient
    private SignedInUserDetail signedInUserDetail;
    public void setOperator(SignedInUserDetail signedInUserDetail) { this.signedInUserDetail = signedInUserDetail; }
    @Transient
    public List<String> errs = new ArrayList();

    @Column(name = "processed_at")
    private Long processedAt;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinTable(
            name="master_posts_join",
            joinColumns = @JoinColumn(name="post_id"),
            inverseJoinColumns = @JoinColumn(name="master_id")
    )
    private SubscriptionMaster master;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinTable(
            name="slave_posts_join",
            joinColumns = @JoinColumn(name="post_id"),
            inverseJoinColumns = @JoinColumn(name="slave_id")
    )
    private SubscriptionSlave slave;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinTable(
            name = "posts_user_join",
            joinColumns = @JoinColumn(name = "post_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private User user;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long postId;

    @Column(name = "title")
    private String title;
    @Column(name = "description")
    private String description;
    @OrderColumn
    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "post_contents", joinColumns = @JoinColumn(name = "post_id"))
    @Column(name = "content")
    private List<String> contents = new ArrayList();

    @ManyToMany(mappedBy = "posts", fetch = FetchType.LAZY)
    private Set<Ticker> tickers = new HashSet();
    @ManyToMany(mappedBy = "posts", fetch = FetchType.LAZY)
    private Set<Tag> tags = new HashSet();

    @Column(name = "updated_at")
    private Long updatedAt;
    @Column(name = "uploaded_at")
    private Long uploadedAt;
    @Column(name = "updated_by")
    private Long updatedBy;
    @Column(name = "uploaded_by")
    private Long uploadedBy;

    @Column(name = "is_active")
    private Boolean active = true;

    private Post(
            SignedInUserDetail signedInUserDetail,
            Long processedAt, 
            SubscriptionSlave slave, User user,
            String title, String description, List<String> contents,
            Set<Ticker> tickers, Set<Tag> tags) {
        this.signedInUserDetail = signedInUserDetail;
        this.processedAt = processedAt;
        
        this.master = slave.getMaster();
        this.slave = slave;
        this.user = user;

        this.title = title;
        this.description = description;
        this.contents = contents;

        this.tickers = tickers.stream().map(ticker -> {
            ticker.addPost(this);
            return ticker;
        }).collect(Collectors.toSet());

        this.tags = tags.stream().map(tag -> {
            tag.addPost(this);
            return tag;
        }).collect(Collectors.toSet());

        uploadedBy = signedInUserDetail.getUserId();
        uploadedAt = Instant.now().toEpochMilli();
        updatedBy = uploadedBy;
        updatedAt = uploadedAt;
        
        onPostCreated();
    }
    private void onPostCreated() {
        registerEvent(new PostCreatedEvent(this, this));
    }

    public Post addMaster(SubscriptionMaster master){
        setMaster(master);
        return this;
    }

    public static Builder getBuilder() { 
        return new Builder(); 
    }
    public static class Builder {

        private SignedInUserDetail signedInUserDetail;
        public void setOperator(SignedInUserDetail signedInUserDetail) {
            this.signedInUserDetail = signedInUserDetail;
        }
        private List<String> errs = new ArrayList();

        private Long processedAt;
        private SubscriptionSlave slave;
        private User user;

        private String title;
        private String description;
        private List<String> contents = new ArrayList();

        private Set<Ticker> tickers = new HashSet();
        private Set<Tag> tags = new HashSet();

        public Builder setProcessedAt(Long processedAt) {
            this.processedAt = processedAt;
            return this;
        }

        public Builder setSlave(SubscriptionSlave slave) {
            this.slave = slave;
            return this;
        }

        public Builder setUser(User user) {
            this.user = user;
            return this;
        }

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setDescription(String description) {
            this.description = description;
            return this;
        }

        public Builder setContents(List<String> contents) {
            this.contents = contents;
            return this;
        }

        public Builder setTickers(Set<Ticker> tickers){
            this.tickers = tickers;
            return this;
        }

        public Builder setTags(Set<Tag> tags) {
            this.tags = tags;
            return this;
        }

        public Post build() {
            if(!slave.isOwner(user)) {
                errs.add("Build failed(Not the slave owner)");
            }
            if(slave.isExpired()) {
                errs.add("Build failed(Subscription expired)");
            }
            if(!slave.hasPermit(SubscriptionPermitEnum.CREATE_THREAD)) {
                errs.add("Build failed(Require permit)");
            }

            if(errs.size() > 0) {
                throw new DomainInvariantException(errs.toString());
            }

            return new Post(
                    signedInUserDetail,
                    processedAt, 
                    slave, user, 
                    title, description, contents,
                    tickers, tags);
        }

    }
}
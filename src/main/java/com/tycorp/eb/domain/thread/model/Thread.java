package com.tycorp.eb.domain.thread.model;

import com.tycorp.eb.domain.meta_data.model.Tag;
import com.tycorp.eb.domain.meta_data.model.Ticker;
import com.tycorp.eb.domain.subscription.model.SubscriptionMaster;
import com.tycorp.eb.domain.subscription.model.SubscriptionPermitEnum;
import com.tycorp.eb.domain.subscription.model.SubscriptionSlave;
import com.tycorp.eb.domain.user.model.EbUser;
import com.tycorp.eb.domain.user.model.EbUserRoleEnum;
import com.tycorp.eb.domain.AbstractEbDomainAggregateTemplate;
import com.tycorp.eb.domain.thread.event.*;
import com.tycorp.eb.domain.user.model.LoginedEbUserDetail;
import lombok.*;
import no.gorandalum.fluentresult.Result;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "thread")
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PRIVATE)
@Where(clause = "is_active=1")
public class Thread extends AbstractEbDomainAggregateTemplate {

    @Transient
    private LoginedEbUserDetail loginedEbUserDetail;
    public void setOperator(LoginedEbUserDetail loginedEbUserDetail) { this.loginedEbUserDetail = loginedEbUserDetail; }
    @Transient
    public List<String> errs = new ArrayList();


    @Column(name = "processed_at")
    private Long processedAt;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinTable(
            name="master_threads_join",
            joinColumns = @JoinColumn(name="thread_id"),
            inverseJoinColumns = @JoinColumn(name="master_id")
    )
    private SubscriptionMaster master;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinTable(
            name="slave_threads_join",
            joinColumns = @JoinColumn(name="thread_id"),
            inverseJoinColumns = @JoinColumn(name="slave_id")
    )
    private SubscriptionSlave slave;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinTable(
            name = "threads_user_join",
            joinColumns = @JoinColumn(name = "thread_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private EbUser user;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "thread_id")
    private Long threadId;

    @Column(name = "title")
    private String title;
    @Column(name = "description")
    private String description;
    @OrderColumn
    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "thread_contents", joinColumns = @JoinColumn(name = "thread_id"))
    @Column(name = "content")
    private List<String> contents = new ArrayList();

    @ManyToMany(mappedBy = "threads", fetch = FetchType.LAZY)
    private Set<Ticker> tickers = new HashSet();
    @ManyToMany(mappedBy = "threads", fetch = FetchType.LAZY)
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


    private Thread(
            LoginedEbUserDetail loginedEbUserDetail,
            Long processedAt, EbUser user, SubscriptionSlave slave,
            String title, String description, List<String> contents,
            Set<Ticker> tickers, Set<Tag> tags) {
        this.loginedEbUserDetail = loginedEbUserDetail;
        
        this.processedAt = processedAt;
        this.user = user;
        this.master = slave.getMaster();
        this.slave = slave;

        this.title = title;
        this.description = description;
        this.contents = contents;

        this.tickers.forEach(ticker -> tickers.add(ticker.addThread(this)));
        this.tags.forEach(tag -> tags.add(tag.addThread(this)));

        uploadedBy = loginedEbUserDetail.getUserId();
        uploadedAt = Instant.now().toEpochMilli();
        updatedBy = uploadedBy;
        updatedAt = uploadedAt;
        
        onThreadCreated();
    }


    public Thread update(String title, String description, List<String> contents) {
        this.title = title;
        this.description = description;
        this.contents = contents;

        onThreadUpdated();
        return this;
    }


    public Thread addMaster(SubscriptionMaster master){
        setMaster(master);
        return this;
    }


    public Thread addTickers(Set<Ticker> tickers) {
        tickers.forEach(ticker -> getTickers().add(ticker.addThread(this)));
        onThreadUpdated();
        return this;
    }
    public Thread removeTickers(Set<Ticker> tickers) {
        tickers.forEach(ticker -> getTickers().remove(ticker.removeThread(this)) );
        onThreadUpdated();
        return this;
    }
    public Thread addTags(Set<Tag> tags) {
        tags.forEach(tag -> getTags().add(tag.addThread(this)));
        onThreadUpdated();
        return this;
    }
    public Thread removeTags(Set<Tag> tags) {
        tags.forEach(tag -> getTags().remove(tag.removeThread(this)) );
        onThreadUpdated();
        return this;
    }


    private boolean canUpdate() {
        return loginedEbUserDetail != null
                && slave.hasPermit(SubscriptionPermitEnum.UPDATE_THREAD)
                && (loginedEbUserDetail.getUserRole().equals(EbUserRoleEnum.ADMIN)
                || loginedEbUserDetail.getUserId() == this.user.getUserId())
                ? true : false;
    }


    public Result<Thread, List<String>> next() {
        List<String> errs = new ArrayList();
        if(containsEventType(ThreadUpdatedEvent.class) && !canUpdate()) {
            errs.add("Update fail(require authorization)");
        }

        updatedBy = getLoginedEbUserDetail().getUserId();
        updatedAt = Instant.now().toEpochMilli();
        return errs.size() == 0 ? Result.success(this) : Result.error(errs);
    }


    private void onThreadCreated() { 
        registerEvent(new ThreadCreatedEvent(this, this)); 
    }
    private void onThreadUpdated() { 
        registerEvent(new ThreadUpdatedEvent(this, this)); 
    }


    public static Builder getBuilder() { return new Builder(); }
    public static class Builder {

        private LoginedEbUserDetail loginedEbUserDetail;
        public void setOperator(LoginedEbUserDetail loginedEbUserDetail) { this.loginedEbUserDetail = loginedEbUserDetail; }
        private List<String> errs = new ArrayList();


        private Long processedAt;
        private EbUser user;
        private SubscriptionSlave slave;

        private String title;
        private String description;
        private List<String> contents = new ArrayList();

        private Set<Ticker> tickers = new HashSet();
        private Set<Tag> tags = new HashSet();


        public Builder setProcessedAt(Long processedAt) {
            this.processedAt = processedAt;
            return this;
        }
        public Builder setEbUser(EbUser user) {
            this.user = user;
            return this;
        }
        public Builder setSlave(SubscriptionSlave slave) {
            this.slave = slave;
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


        public Result<Builder, List<String>> next() {
            if(!slave.isOwner(user)) {
                errs.add("Build failed(Not the slave owner)");
            }
            if(slave.isExpired()) {
                errs.add("Build failed(Subscription expired)");
            }
            if(!slave.hasPermit(SubscriptionPermitEnum.CREATE_THREAD)) {
                errs.add("Build failed(Require permit)");
            }

            return loginedEbUserDetail != null
                    && (loginedEbUserDetail.getUserRole().equals(EbUserRoleEnum.ADMIN)
                    || (loginedEbUserDetail.getUserId() == user.getUserId()
                    && loginedEbUserDetail.getUseremail().equals(user.getUseremail())))
                    ? Result.success(this) : Result.error(errs);
        }


        public Thread build() {
            return new Thread(
                    loginedEbUserDetail,
                    processedAt, user, slave,
                    title, description, contents,
                    tickers, tags);
        }

    }
}
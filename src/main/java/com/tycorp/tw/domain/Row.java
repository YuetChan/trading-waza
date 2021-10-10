package com.tycorp.tw.domain;

import com.tycorp.tw.exception.DomainInvariantException;
import com.tycorp.tw.domain.event.RowCreatedEvent;
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
@Table(name = "row")
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PRIVATE)
@Where(clause = "is_active=1")
public class Row extends AbstractDomainEntityTemplate {

    @Transient
    private SignedInUserDetail signedInUserDetail;
    public void setOperator(SignedInUserDetail signedInUserDetail) {
        setSignedInUserDetail(signedInUserDetail);
    }
    @Transient
    public List<String> errs = new ArrayList();

    @Column(name = "processed_at")
    private Long processedAt;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinTable(
            name="master_rows_join",
            joinColumns = @JoinColumn(name="row_id"),
            inverseJoinColumns = @JoinColumn(name="master_id")
    )
    private SubscriptionMaster master;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinTable(
            name="slave_rows_join",
            joinColumns = @JoinColumn(name="row_id"),
            inverseJoinColumns = @JoinColumn(name="slave_id")
    )
    private SubscriptionSlave slave;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinTable(
            name = "rows_user_join",
            joinColumns = @JoinColumn(name = "row_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private User user;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "row_id")
    private Long rowId;

    @ManyToMany(mappedBy = "rows", fetch = FetchType.LAZY)
    private Set<Ticker> tickers = new HashSet();
    @ManyToMany(mappedBy = "rows", fetch = FetchType.LAZY)
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

    private Row(
            SignedInUserDetail signedInUserDetail,
            Long processedAt,
            SubscriptionSlave slave, User user,
            Set<Ticker> tickers, Set<Tag> tags) {
        setSignedInUserDetail(signedInUserDetail);
        setProcessedAt(processedAt);

        setMaster(slave.getMaster());
        setSlave(slave);
        setUser(user);

        setTickers(tickers.stream().map(ticker -> {
            ticker.addRow(this);
            return ticker;
        }).collect(Collectors.toSet()));

        setTags(tags.stream().map(tag -> {
            tag.addRow(this);
            return tag;
        }).collect(Collectors.toSet()));

        setUpdatedBy(signedInUserDetail.getUserId());
        setUpdatedAt(Instant.now().toEpochMilli());
        setUploadedBy(getUpdatedBy());
        setUploadedAt(getUpdatedAt());
        
        onRowCreated();
    }
    private void onRowCreated() {
        registerEvent(new RowCreatedEvent(this, this));
    }

    public Row addMaster(SubscriptionMaster master){
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

        public Builder setTickers(Set<Ticker> tickers){
            this.tickers = tickers;
            return this;
        }

        public Builder setTags(Set<Tag> tags) {
            this.tags = tags;
            return this;
        }

        public Row build() {
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

            return new Row(
                    signedInUserDetail,
                    processedAt, 
                    slave, user,
                    tickers, tags);
        }

    }
}
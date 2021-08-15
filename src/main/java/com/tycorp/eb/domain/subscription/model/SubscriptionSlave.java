package com.tycorp.eb.domain.subscription.model;

import com.tycorp.eb.domain.AbstractEbDomainAggregateTemplate;
import com.tycorp.eb.domain.subscription.event.SubscriptionCreatedEvent;
import com.tycorp.eb.domain.subscription.event.SubscriptionExpiredEvent;
import com.tycorp.eb.domain.subscription.event.SubscriptionExtendedEvent;
import com.tycorp.eb.domain.subscription.event.SubscriptionPermitsAddedEvent;
import com.tycorp.eb.domain.thread.model.Thread;
import com.tycorp.eb.domain.user.model.EbUser;
import com.tycorp.eb.domain.user.model.EbUserRoleEnum;
import com.tycorp.eb.domain.user.model.LoginedEbUserDetail;

import lombok.*;
import no.gorandalum.fluentresult.Result;

import javax.persistence.*;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "slave")
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PUBLIC)
public class SubscriptionSlave extends AbstractEbDomainAggregateTemplate {

    @Transient
    private LoginedEbUserDetail loginedEbUserDetail;
    public void setOperator(LoginedEbUserDetail loginedEbUserDetail) { setLoginedEbUserDetail(loginedEbUserDetail); }
    @Transient
    private List<String> errs = new ArrayList();


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "slave_id")
    private Long slaveId;

    @ManyToOne
    @JoinTable(
            name = "owner_slaves_join",
            joinColumns = @JoinColumn(name = "slave_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private EbUser owner;
    @ManyToOne
    @JoinTable(
            name = "master_slaves_join",
            joinColumns = @JoinColumn(name = "slave_id"),
            inverseJoinColumns = @JoinColumn(name = "master_id")
    )
    private SubscriptionMaster master;
    @OneToMany(mappedBy = "slave")
    private Set<Thread> threads = new HashSet();

    @ElementCollection
    @CollectionTable(
            name = "slave_permits",
            joinColumns = @JoinColumn(name = "slave_id")
    )
    @Column(name = "permit")
    @Enumerated(EnumType.STRING)
    private List<SubscriptionPermitEnum> permits = new ArrayList();

    @Column(name = "expired_at")
    private Long expiredAt;


    @Column(name = "uploaded_at")
    private Long uploadedAt;
    @Column(name = "updated_at")
    private Long updatedAt;
    @Column(name = "uploaded_by")
    private Long uploadedBy = -1l;
    @Column(name = "updated_by")
    private Long updatedBy = -1l;


    public SubscriptionSlave(SubscriptionMaster master) {
        master.addSlave(this);
        this.master = master;

        expiredAt = Instant.now().toEpochMilli();

        uploadedAt = expiredAt;
        updatedAt = uploadedAt;

        onSubscriptionCreated();
    }


    public boolean isOwner(EbUser user) {
        return user.getUserId() == getOwner().getUserId();
    }

    public SubscriptionSlave addOwner(EbUser owner) {
        setOwner(owner);
        return this;
    }
    public SubscriptionSlave addMaster(SubscriptionMaster master) {
        setMaster(master);
        return this;
    }


    public boolean hasPermit(SubscriptionPermitEnum permit){
        return getPermits().contains(permit);
    }
    public boolean isExpired() {
        return Instant.ofEpochMilli(expiredAt).isBefore(Instant.now());
    }

    public SubscriptionSlave addPermits(List<SubscriptionPermitEnum> permitEnums) {
        getPermits().addAll(permitEnums);
        onPermitsAdded();
        return this;
    }
    public boolean canAddPermit() {
        return loginedEbUserDetail != null
                && (loginedEbUserDetail.getUserRole().equals(EbUserRoleEnum.ADMIN)
                || loginedEbUserDetail.getUserId() == master.getOwner().getUserId())
                ? true : false;
    }

    public SubscriptionSlave extend(int days) {
        setExpiredAt(Instant.now().plus(days, ChronoUnit.DAYS).toEpochMilli());
        onSubscriptionExtended();
        return this;
    }
    public boolean canExtend() {
        return loginedEbUserDetail != null
                && (loginedEbUserDetail.getUserRole().equals(EbUserRoleEnum.ADMIN)
                || loginedEbUserDetail.getUserId() == owner.getUserId())
                ? true : false;
    }


    private void onSubscriptionCreated() {
        registerEvent(new SubscriptionCreatedEvent(this, this));
    }
    private void onSubscriptionExtended() {
        registerEvent(new SubscriptionExpiredEvent(this, this));
    }
    private void onPermitsAdded() {
        registerEvent(new SubscriptionPermitsAddedEvent(this, this));
    }


    public Result<SubscriptionSlave, List<String>> next() {
        if(containsEventType(SubscriptionPermitsAddedEvent.class) && !canAddPermit()) {
            errs.add("Add permit failed(require authorization)");
        }
        if(containsEventType(SubscriptionExtendedEvent.class) && !canExtend()) {
            errs.add("Extend failed(require authorization)");
        }

        setUpdatedAt(Instant.now().toEpochMilli());
        setUpdatedBy(getLoginedEbUserDetail().getUserId());

        return errs.size() == 0 ? Result.success(this) : Result.error(errs);
    }


    @Override
    public boolean equals(Object other) { return slaveId == ((SubscriptionSlave)other).getSlaveId(); }

}

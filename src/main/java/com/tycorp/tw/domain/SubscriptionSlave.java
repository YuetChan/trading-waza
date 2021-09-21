package com.tycorp.tw.domain;

import com.tycorp.tw.domain.event.SubscriptionCreatedEvent;
import com.tycorp.tw.domain.event.SubscriptionExpiredEvent;
import com.tycorp.tw.domain.event.SubscriptionPermitsAddedEvent;

import com.tycorp.tw.exception.DomainInvariantException;
import lombok.*;

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
public class SubscriptionSlave extends AbstractDomainEntityTemplate {

    @Transient
    private SignedInUserDetail signedInUserDetail;
    public void setOperator(SignedInUserDetail signedInUserDetail) { setSignedInUserDetail(signedInUserDetail); }
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
    private User owner;
    @ManyToOne
    @JoinTable(
            name = "master_slaves_join",
            joinColumns = @JoinColumn(name = "slave_id"),
            inverseJoinColumns = @JoinColumn(name = "master_id")
    )
    private SubscriptionMaster master;
    @OneToMany(mappedBy = "slave")
    private Set<Post> posts = new HashSet();

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
    @Column(name = "uploaded_by")
    private Long uploadedBy = -1l;

    public SubscriptionSlave(SubscriptionMaster master) {
        master.addSlave(this);
        setMaster(master);

        Long nowInEpochMilli = Instant.now().toEpochMilli();
        setExpiredAt(nowInEpochMilli);
        setUploadedAt(nowInEpochMilli);

        onSubscriptionCreated();
    }
    private void onSubscriptionCreated() {
        registerEvent(new SubscriptionCreatedEvent(this, this));
    }

    public boolean isOwner(User user) {
        return user.getUserId() == getOwner().getUserId();
    }

    public boolean hasPermit(SubscriptionPermitEnum permit) {
        return getPermits().contains(permit);
    }

    public boolean isExpired() {
        return Instant.ofEpochMilli(getExpiredAt()).isBefore(Instant.now());
    }

    public void addOwner(User owner) {
        setOwner(owner);
    }

    public void addMaster(SubscriptionMaster master) {
        setMaster(master);
    }

    public void addPermits(List<SubscriptionPermitEnum> permitEnums) {
        if(canAddPermit()) {
            getPermits().addAll(permitEnums);
            onPermitsAdded();
        }else {
            throw new DomainInvariantException("Cant add permit to subscription(Require authorization)");
        }
    }
    private void onPermitsAdded() {
        registerEvent(new SubscriptionPermitsAddedEvent(this, this));
    }
    public boolean canAddPermit() {
        return getSignedInUserDetail() != null
                && getSignedInUserDetail().getUserRole().equals(UserRoleEnum.ADMIN)
                ? true : false;
    }

    public void extend(int days) {
        if(canExtend()){
            setExpiredAt(Instant.now().plus(days, ChronoUnit.DAYS).toEpochMilli());
            onSubscriptionExtended();
        }else {
            throw new DomainInvariantException("Cant extend subscription(Require authorization)");
        }
    }
    private void onSubscriptionExtended() {
        registerEvent(new SubscriptionExpiredEvent(this, this));
    }
    public boolean canExtend() {
        return getSignedInUserDetail() != null
                && getSignedInUserDetail().getUserRole().equals(UserRoleEnum.ADMIN)
                ? true : false;
    }

    @Override
    public boolean equals(Object other) {
        return getSlaveId() == ((SubscriptionSlave)other).getSlaveId();
    }

}

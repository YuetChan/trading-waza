package com.tycorp.eb.domain.subscription.model;

import com.tycorp.eb.domain.thread.model.Thread;
import com.tycorp.eb.domain.meta_data.model.Tag;
import com.tycorp.eb.domain.meta_data.model.Ticker;
import com.tycorp.eb.domain.user.model.EbUser;
import com.tycorp.eb.domain.user.model.LoginedEbUserDetail;
import lombok.*;

import javax.persistence.*;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "master")
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PUBLIC)
public class SubscriptionMaster {

    @Transient
    private LoginedEbUserDetail loginedEbUserDetail;


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "master_id")
    private Long masterId;

    @Column(name = "name")
    private String name;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinTable(
            name = "owner_masters_join",
            joinColumns = @JoinColumn(name = "master_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private EbUser owner = null;
    @OneToMany(mappedBy = "master", fetch = FetchType.LAZY)
    private Set<SubscriptionSlave> slaves = new HashSet();
    @OneToMany(mappedBy = "master", fetch = FetchType.LAZY)
    private Set<Thread> threads = new HashSet();

    @ManyToMany(mappedBy = "masters", fetch = FetchType.LAZY)
    private Set<Ticker> tickers = new HashSet();
    @ManyToMany(mappedBy = "masters", fetch = FetchType.LAZY)
    private Set<Tag> tags = new HashSet();


    @Column(name = "uploaded_at")
    private Long uploadedAt;
    @Column(name = "uploaded_by")
    private Long uploadedBy = -1l;


    public SubscriptionMaster(String name){
        this.name = name;
        uploadedAt = Instant.now().toEpochMilli();
    }


    public SubscriptionMaster addSlave(SubscriptionSlave slave) {
        getSlaves().add(slave.addMaster(this));
        return this;
    }
    public SubscriptionMaster addThread(Thread thread) {
        getThreads().add(thread.addMaster(this));
        return this;
    }


    public SubscriptionMaster addTickers(Set<Ticker> tickers){
        tickers.forEach(ticker -> getTickers().add(ticker.addMaster(this)));
        return this;
    }
    public SubscriptionMaster addTags(Set<Tag> tags){
        tags.forEach(tag -> getTags().add(tag.addMaster(this)));
        return this;
    }

}

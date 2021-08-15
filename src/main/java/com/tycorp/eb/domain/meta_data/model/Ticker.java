package com.tycorp.eb.domain.meta_data.model;

import com.google.gson.annotations.Expose;
import com.tycorp.eb.domain.subscription.model.SubscriptionMaster;
import com.tycorp.eb.domain.thread.model.Thread;
import lombok.*;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "ticker")
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PUBLIC)
@Where(clause = "is_active=1")
public class Ticker {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ticker_id")
    @Expose
    private Long tickerId;
    @Column(name = "name")
    @Expose
    private String name;

    @ManyToMany
    @JoinTable(
            name="masters_tickers_join",
            joinColumns = @JoinColumn(name = "ticker_id"),
            inverseJoinColumns = @JoinColumn(name = "master_id")
    )
    private Set<SubscriptionMaster> masters = new HashSet();
    @ManyToMany
    @JoinTable(
            name = "threads_tickers_join",
            joinColumns = @JoinColumn(name = "ticker_id"),
            inverseJoinColumns = @JoinColumn(name = "thread_id"))
    private Set<Thread> threads = new HashSet();


    @Column(name = "is_active")
    private Boolean active = true;


    public Ticker(String name) {
        this.name = name;
    }

    public Ticker addThread(Thread thread) {
        getThreads().add(thread);
        return this;
    }
    public Ticker removeThread(Thread thread) {
        getThreads().remove(thread);
        return this;
    }

    public Ticker addMaster(SubscriptionMaster master) {
        getMasters().add(master);
        return this;
    }

}



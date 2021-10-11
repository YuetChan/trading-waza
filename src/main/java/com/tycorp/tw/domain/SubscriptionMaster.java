package com.tycorp.tw.domain;

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
    private User owner = null;
    @OneToMany(mappedBy = "master", fetch = FetchType.LAZY)
    private Set<SubscriptionSlave> slaves = new HashSet();
    @OneToMany(mappedBy = "master", fetch = FetchType.LAZY)
    private Set<Row> rows = new HashSet();

    @ManyToMany(mappedBy = "masters", fetch = FetchType.LAZY)
    private Set<Ticker> tickers = new HashSet();
    @ManyToMany(mappedBy = "masters", fetch = FetchType.LAZY)
    private Set<Indicator> indicators = new HashSet();

    @Column(name = "uploaded_at")
    private Long uploadedAt;
    @Column(name = "uploaded_by")
    private Long uploadedBy = -1l;

    public SubscriptionMaster(String name) {
        setName(name);
        setUploadedAt(Instant.now().toEpochMilli());
    }

    public void addSlave(SubscriptionSlave slave) {
        slave.addMaster(this);
        getSlaves().add(slave);
    }

    public void addRow(Row row) {
        getRows().add(row.addMaster(this));
    }

    public void addTickers(Set<Ticker> tickers) {
        tickers.forEach(ticker -> {
            ticker.addMaster(this);
            getTickers().add(ticker);
        });
    }

    public void addTags(Set<Indicator> indicators) {
        indicators.forEach(indicator -> {
            indicator.addMaster(this);
            getIndicators().add(indicator);
        });
    }

}
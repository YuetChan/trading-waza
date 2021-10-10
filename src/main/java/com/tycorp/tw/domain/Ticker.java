package com.tycorp.tw.domain;

import com.google.gson.annotations.Expose;
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

    @Expose
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ticker_id")
    private Long tickerId;

    @ManyToMany
    @JoinTable(
            name="masters_tickers_join",
            joinColumns = @JoinColumn(name = "ticker_id"),
            inverseJoinColumns = @JoinColumn(name = "master_id")
    )
    private Set<SubscriptionMaster> masters = new HashSet();
    @ManyToMany
    @JoinTable(
            name = "rows_tickers_join",
            joinColumns = @JoinColumn(name = "ticker_id"),
            inverseJoinColumns = @JoinColumn(name = "row_id"))
    private Set<Row> rows = new HashSet();

    @Expose
    @Column(name = "name")
    private String name;

    @Column(name = "is_active")
    private Boolean active = true;

    public Ticker(String name) {
        setName(name);
    }

    public void addMaster(SubscriptionMaster master) {
        getMasters().add(master);
    }

    public void addRow(Row row) {
        getRows().add(row);
    }

}



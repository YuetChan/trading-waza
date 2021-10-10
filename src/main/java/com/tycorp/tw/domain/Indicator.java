package com.tycorp.tw.domain;

import com.google.gson.annotations.Expose;
import lombok.*;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "indicator")
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PUBLIC)
@Where(clause = "is_active=1")
public class Indicator {

    @Expose
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "indicator_id")
    private Long indicatorId;

    @ManyToMany
    @JoinTable(
            name = "masters_indicators_join",
            joinColumns = @JoinColumn(name = "indicator_id"),
            inverseJoinColumns = @JoinColumn(name = "master_id")
    )
    private Set<SubscriptionMaster> masters = new HashSet();
    @ManyToMany
    @JoinTable(
            name = "rows_indicators_join",
            joinColumns = @JoinColumn(name = "indicator_id"),
            inverseJoinColumns = @JoinColumn(name = "row_id"))
    private Set<Row> rows = new HashSet();

    @Expose
    @Column(name = "name")
    private String name;

    @Column(name = "is_active")
    private Boolean active = true;

    public Indicator(String name) {
        setName(name);
    }

    public void addMaster(SubscriptionMaster master) {
        getMasters().add(master);
    }

    public void addRow(Row row) {
        getRows().add(row);
    }

}

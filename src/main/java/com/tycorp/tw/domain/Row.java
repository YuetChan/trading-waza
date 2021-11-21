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
@Table(name = "tw_row")
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
            name = "rows_user_join",
            joinColumns = @JoinColumn(name = "row_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private User user;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "row_id")
    private Long rowId;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinTable(
            name = "rows_ticker_join",
            joinColumns = @JoinColumn(name = "row_id"),
            inverseJoinColumns = @JoinColumn(name = "ticker_id"))
    private Ticker ticker;
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "open", column = @Column(name = "tw_open")),
            @AttributeOverride(name = "high", column = @Column(name = "tw_high")),
            @AttributeOverride(name = "close", column = @Column(name = "tw_close")),
            @AttributeOverride(name = "low", column = @Column(name = "tw_low")),
            @AttributeOverride(name = "change", column = @Column(name = "tw_change"))
    })
    private PriceDetail priceDetail;
    @ManyToMany(mappedBy = "rows", fetch = FetchType.LAZY)
    private Set<Indicator> indicators = new HashSet();

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
            User user,
            Ticker ticker, PriceDetail priceDetail,
            Set<Indicator> indicators) {
        setSignedInUserDetail(signedInUserDetail);
        setProcessedAt(processedAt);

        setUser(user);

        setTicker(ticker);
        setPriceDetail(priceDetail);
        
        setIndicators(indicators.stream().map(indicator -> {
            indicator.addRow(this);
            return indicator;
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
        private User user;

        private Ticker ticker;
        private PriceDetail priceDetail;
        private Set<Indicator> indicators = new HashSet();

        public Builder setProcessedAt(Long processedAt) {
            this.processedAt = processedAt;
            return this;
        }

        public Builder setUser(User user) {
            this.user = user;
            return this;
        }

        public Builder setTicker(Ticker ticker) {
            this.ticker = ticker;
            return this;
        }

        public Builder setPriceDetail(PriceDetail priceDetail) {
            this.priceDetail = priceDetail;
            return this;
        }

        public Builder setIndicators(Set<Indicator> indicators) {
            this.indicators = indicators;
            return this;
        }

        public Row build() {
            if(errs.size() > 0) {
                throw new DomainInvariantException(errs.toString());
            }

            return new Row(signedInUserDetail, processedAt, user, ticker, priceDetail, indicators);
        }

    }
}
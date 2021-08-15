package com.tycorp.eb.domain.user.model;

import com.tycorp.eb.domain.AbstractEbDomainAggregateTemplate;
import com.tycorp.eb.domain.subscription.model.SubscriptionMaster;
import com.tycorp.eb.domain.subscription.model.SubscriptionSlave;
import com.tycorp.eb.domain.user.event.*;
import lombok.*;
import no.gorandalum.fluentresult.Result;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "user")
@AllArgsConstructor
@NoArgsConstructor
@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PUBLIC)
public class EbUser extends AbstractEbDomainAggregateTemplate implements UserDetails {

    @Transient
    private LoginedEbUserDetail loginedEbUserDetail;
    public EbUser setOperator(LoginedEbUserDetail loginedEbUserDetail) {
        this.loginedEbUserDetail = loginedEbUserDetail;
        return this;
    }
    @Transient
    public List<String> errs = new ArrayList();


    @OneToMany(mappedBy = "owner", fetch = FetchType.LAZY)
    private Set<SubscriptionMaster> masters = new HashSet();
    @OneToMany(mappedBy = "owner", fetch = FetchType.LAZY)
    private Set<SubscriptionSlave> slaves = new HashSet();


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;
    @Column(name = "useremail")
    private String useremail;
    @Column(name = "password")

    private String password;
    @Column(name = "user_role")
    @Enumerated(EnumType.STRING)
    private EbUserRoleEnum userRole;

    @AttributeOverrides({
            @AttributeOverride(name = "activated", column = @Column(name = "activated")),
            @AttributeOverride(name = "locked", column = @Column(name = "locked")),
            @AttributeOverride(name = "expired", column = @Column(name = "expired")),
            @AttributeOverride(name = "passwordExpired", column = @Column(name = "passwordExpired"))
    })
    private EbUserMeta meta = new EbUserMeta();

    @Column(name = "username")
    private String username;


    public EbUser(
            Set<SubscriptionSlave> slaves,
            String useremail, String password,
            String username) {
        slaves.forEach(slave -> slaves.add(slave.addOwner(this)));

        this.useremail = useremail;
        this.password = password;
        userRole = EbUserRoleEnum.USER;

        this.username = username;
    }
    public EbUser(Long userId, String useremail, EbUserRoleEnum userRole) {
        this.userId = userId;
        this.useremail = useremail;
        this.userRole = userRole;
    }


    public EbUser validatePassword(String password) {
        if(!this.password.equals(password)) {
            errs.add("Invalid crediential");
        }

        onPasswordValidated();
        return this;
    }
    private void onPasswordValidated() {
        registerEvent(new EbUserPasswordValidatedEvent(this, this));
    }


    public Result<EbUser, List<String>> next() {
        return errs.size() == 0 ? Result.success(this) : Result.error(errs);
    }


    @Override
    public String getUsername() { return username; }
    @Override
    public String getPassword() { return password; }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() { return null; }

    @Override
    public boolean isAccountNonExpired() { return true; }
    @Override
    public boolean isAccountNonLocked() { return meta.isLocked(); }
    @Override
    public boolean isCredentialsNonExpired() { return meta.isPasswordExpired(); }

    @Override
    public boolean isEnabled() { return true; }

}

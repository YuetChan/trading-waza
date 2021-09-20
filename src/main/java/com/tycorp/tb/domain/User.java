package com.tycorp.tb.domain;

import com.tycorp.tb.domain.event.UserCreatedEvent;
import lombok.*;
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
public class User extends AbstractDomainEntityTemplate implements UserDetails {

    @Transient
    private SignedInUserDetail signedInUserDetail;
    public void setOperator(SignedInUserDetail signedInUserDetail) {
        setSignedInUserDetail(signedInUserDetail);
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
    private UserRoleEnum userRole;

    @AttributeOverrides({
            @AttributeOverride(name = "activated", column = @Column(name = "activated")),
            @AttributeOverride(name = "locked", column = @Column(name = "locked")),
            @AttributeOverride(name = "expired", column = @Column(name = "expired")),
            @AttributeOverride(name = "passwordExpired", column = @Column(name = "passwordExpired"))
    })
    private UserMeta meta = new UserMeta();

    @Column(name = "username")
    private String username;

    public User(
            Set<SubscriptionSlave> slaves,
            String useremail, String password,
            String username) {
        slaves.forEach(slave -> {
            slave.addOwner(this);
        });
        setSlaves(slaves);

        setUseremail(useremail);
        setPassword(password);
        setUserRole(UserRoleEnum.USER);

        setUsername(username);

        onUserCreated();
    }
    private void onUserCreated() {
        registerEvent(new UserCreatedEvent(this, this));
    }

    public User(Long userId, String useremail, UserRoleEnum userRole) {
        setUserId(userId);
        setUseremail(useremail);
        setUserRole(userRole);
    }

    public boolean isPasswordValid(String password) {
        if(!getPassword().equals(password)) {
            getErrs().add("Invalid crediential");
            return false;
        }

        return true;
    }

    @Override
    public String getUsername() {
        return username;
    }
    @Override
    public String getPassword() {
        return password;
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }
    @Override
    public boolean isAccountNonLocked() {
        return meta.isLocked();
    }
    @Override
    public boolean isCredentialsNonExpired() {
        return meta.isPasswordExpired();
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}

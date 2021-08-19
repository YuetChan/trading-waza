package com.tycorp.eb.domain;

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

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ticker_id")
    @Expose
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
            name = "posts_tickers_join",
            joinColumns = @JoinColumn(name = "ticker_id"),
            inverseJoinColumns = @JoinColumn(name = "post_id"))
    private Set<Post> posts = new HashSet();

    @Column(name = "name")
    @Expose
    private String name;

    @Column(name = "is_active")
    private Boolean active = true;

    public Ticker(String name) {
        this.name = name;
    }

    public void addMaster(SubscriptionMaster master) {
        getMasters().add(master);
    }

    public void addPost(Post post) {
        getPosts().add(post);
    }

}


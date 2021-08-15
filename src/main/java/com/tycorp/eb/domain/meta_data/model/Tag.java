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
@Table(name = "tag")
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PUBLIC)
@Where(clause = "is_active=1")
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tag_id")
    @Expose
    private Long tagId;
    @Column(name = "name")
    @Expose
    private String name;


    @ManyToMany
    @JoinTable(
            name = "masters_tags_join",
            joinColumns = @JoinColumn(name = "tag_id"),
            inverseJoinColumns = @JoinColumn(name = "master_id")
    )
    private Set<SubscriptionMaster> masters = new HashSet();
    @ManyToMany
    @JoinTable(
            name = "threads_tags_join",
            joinColumns = @JoinColumn(name = "tag_id"),
            inverseJoinColumns = @JoinColumn(name = "thread_id"))
    private Set<Thread> threads = new HashSet();


    @Column(name = "is_active")
    private Boolean active = true;


    public Tag(String name) {
        this.name = name;
    }

    public Tag addThread(Thread thread) {
        getThreads().add(thread);
        return this;
    }
    public Tag removeThread(Thread thread) {
        getThreads().remove(thread);
        return this;
    }

    public Tag addMaster(SubscriptionMaster master) {
        getMasters().add(master);
        return this;
    }

}

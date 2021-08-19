package com.tycorp.eb.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Embeddable;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
public class UserMeta {

    private boolean activated = true;
    private boolean expired = false;
    private boolean locked = false;
    private boolean passwordExpired = false;

}

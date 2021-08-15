package com.tycorp.eb.app.user.dto.non_exposable;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PUBLIC)
public class EbUserLoginDto {

    private String useremail;
    private String password;

}


package com.tycorp.eb.rest.dto.non_exposable;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PUBLIC)
public class UserSignInDto {

    private String useremail;
    private String password;

}


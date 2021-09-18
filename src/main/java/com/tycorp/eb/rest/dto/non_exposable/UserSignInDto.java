package com.tycorp.eb.rest.dto.non_exposable;

import lombok.*;

import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PUBLIC)
public class UserSignInDto {

    @NotNull
    private String useremail;
    @NotNull
    private String password;

}


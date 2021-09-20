package com.tycorp.tb.rest.dto.non_exposable;

import lombok.*;

import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PUBLIC)
public class UserRegisterDto {

    @NotNull
    private String inviteCode;

    @NotNull
    private String useremail;
    @NotNull
    private String password;

    @NotNull
    private String username;

}

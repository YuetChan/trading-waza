package com.tycorp.eb.app.user.dto.non_exposable;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PUBLIC)
public class EbUserRegisterDto {

    private String code;

    private String useremail;
    private String password;

    private String username;

}

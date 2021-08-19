package com.tycorp.eb.service;
import com.tycorp.eb.exception.InvalidCredentialException;
import com.tycorp.eb.domain.SubscriptionPermitEnum;
import com.tycorp.eb.domain.SubscriptionSlave;
import com.tycorp.eb.repository.SubscriptionSlaveRepository;
import com.tycorp.eb.domain.User;
import com.tycorp.eb.domain.SignedInUser;
import com.tycorp.eb.repository.UserRepository;
import com.tycorp.eb.spring_security.jwt_auth.JwtUtil;
import no.gorandalum.fluentresult.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

@Service
public class UserService extends AbstractEntityService {

    @Autowired
    protected SubscriptionSlaveRepository slaveRepo;
    @Autowired
    protected UserRepository userRepo;

    @Autowired
    protected InviteCodeService inviteCodeSvc;

    public String signIn(String useremail, String password) {
        List<String> jwtCache = new ArrayList();
        try {
            User user = userRepo.findByUseremail(useremail)
                    .orElseThrow(() -> new InvalidCredentialException("Invalid crediential"));
            if(!user.isPasswordValid(password)) {
                throw new InvalidCredentialException("Invalid crediential");
            }

            jwtCache.add(JwtUtil.generateJwtToken(new SignedInUser(user)));
        }catch(Exception ex) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, ex.getMessage());
        }

        return jwtCache.get(0);
    }

    public void register(String inviteCode, String useremail, String password, String username) {
        if(isInviteCodeExisted(useremail, inviteCode)) {
            if(isUserRegistered(useremail)) {
                throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Registered useremail");
            }

            Set<SubscriptionSlave> defaultSlaves = slaveRepo.getDefaultSubscriptionSlaves();
            defaultSlaves.forEach(slave -> {
                slave.setOperator(authFacade.getDefaultAuthenticatedUserDetail());
                slave.extend(99999);
                slave.addPermits(
                        Arrays.asList(
                                SubscriptionPermitEnum.CREATE_THREAD,
                                SubscriptionPermitEnum.UPDATE_THREAD));
            });

            defaultSlaves.forEach(slave -> slaveRepo.save(slave));
            userRepo.save(new User(defaultSlaves, useremail, password, username));
        }else {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Invalid invite code");
        }
    }

    private boolean isInviteCodeExisted(String useremail, String inviteCode){
        return inviteCodeSvc.isInviteCodeValid(useremail, inviteCode);
    }

    private boolean isUserRegistered(String useremail) {
        return userRepo.findByUseremail(useremail).isPresent();
    }

}

package com.tycorp.tb.service;
import com.tycorp.tb.domain.*;
import com.tycorp.tb.exception.InvalidCredentialException;
import com.tycorp.tb.repository.SubscriptionMasterRepository;
import com.tycorp.tb.repository.SubscriptionSlaveRepository;
import com.tycorp.tb.repository.UserRepository;
import com.tycorp.tb.spring_security.jwt_auth.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@Service
public class UserService extends AbstractEntityService {

    @Autowired
    protected SubscriptionMasterRepository masterRepo;
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

            SubscriptionSlave defaultSlave = slaveRepo.getDefaultSubscriptionSlave(masterRepo.getDefaultSubscriptionMasters());
            slaveRepo.save(defaultSlave);

            userRepo.save(new User(Collections.singleton(defaultSlave), useremail, password, username));
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

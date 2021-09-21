package com.tycorp.tw.service;
import com.tycorp.tw.domain.*;
import com.tycorp.tw.exception.InvalidCredentialException;
import com.tycorp.tw.repository.SubscriptionMasterRepository;
import com.tycorp.tw.repository.SubscriptionSlaveRepository;
import com.tycorp.tw.repository.UserRepository;
import com.tycorp.tw.spring_security.jwt_auth.JwtUtil;
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

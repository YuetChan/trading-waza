package com.tycorp.eb.app.user;

import com.tycorp.eb.app.AbstractEbAppAggregateService;
import com.tycorp.eb.app.invite_code.BasicInviteCodeService;
import com.tycorp.eb.domain.subscription.model.SubscriptionPermitEnum;
import com.tycorp.eb.domain.subscription.model.SubscriptionSlave;
import com.tycorp.eb.domain.subscription.repository.SubscriptionSlaveRepository;
import com.tycorp.eb.domain.user.model.EbUser;
import com.tycorp.eb.domain.user.model.LoginedEbUser;
import com.tycorp.eb.domain.user.repository.EbUserRepository;
import com.tycorp.eb.spring_security.jwt_auth.EbJwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public abstract class AbstractEbUserService extends AbstractEbAppAggregateService {

    @Autowired
    protected EbUserRepository userRepo;
    @Autowired
    protected SubscriptionSlaveRepository slaveRepo;

    @Autowired
    protected BasicInviteCodeService inviteCodeSvc;

    protected String login(String useremail, String password) {
        List<String> JWTs = new ArrayList();
        EbUser user = userRepo.findByUseremail(useremail).orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid crediential"));

        user.validatePassword(password)
                .next()
                .runIfSuccess(() -> JWTs.add(EbJwtUtil.generateJwtToken(new LoginedEbUser(user))))
                .consumeError(errs -> {
                    throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, errs.toString());
                });

        return JWTs.get(0);
    }

    protected void register(String inviteCode, String useremail, String password, String username) {
        inviteCodeSvc.validateInviteCode(useremail, inviteCode)
                .runIfSuccess(() -> {
                    if(userRepo.findByUseremail(useremail).isPresent()){
                        throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Registered useremail");
                    }

                    Set<SubscriptionSlave> defaultSlaves = slaveRepo.getDefaultSubscriptions();
                    defaultSlaves.forEach(slave -> {
                        slave.setOperator(authFacade.getDefaultAuthenticatedEbUserDetail());
                        slave.extend(99999).addPermits(
                                Arrays.asList(SubscriptionPermitEnum.CREATE_THREAD, SubscriptionPermitEnum.UPDATE_THREAD));
                        slaveRepo.save(slave);
                    });

                    userRepo.save(new EbUser(defaultSlaves, useremail, password, username));
                })
                .consumeError(errs -> {
                    throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, errs.toString());
                });
    }

}

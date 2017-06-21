package com.tpj.packinglist.web.rest;

import com.tpj.packinglist.domain.PersistentToken;
import com.tpj.packinglist.domain.User;
import com.tpj.packinglist.repository.PersistentTokenRepository;
import com.tpj.packinglist.repository.UserRepository;
import com.tpj.packinglist.security.SecurityUtils;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Api(description = "Users management API")
@RequestMapping("api")
public class SecurityController {


    @Autowired
    private UserRepository userRepo;

    @Autowired
    private PersistentTokenRepository tokenRepo;

    @RequestMapping(value = "security/account", method = RequestMethod.GET)
    public @ResponseBody
    User getUserAccount()  {
        User user = userRepo.findByLogin(SecurityUtils.getCurrentUserLogin());
        user.setPassword(null);
        return user;
    }


    @PreAuthorize("hasAuthority('admin')")
    @RequestMapping(value = "security/tokens", method = RequestMethod.GET)
    public @ResponseBody
    List<PersistentToken> getTokens () {
        List<PersistentToken> tokens = tokenRepo.findAll();
        for(PersistentToken t:tokens) {
            t.setSeries(null);
            t.setTokenValue(null);
        }
        return tokens;
    }



}

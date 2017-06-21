package com.tpj.packinglist.web.rest;

import com.tpj.packinglist.domain.User;
import com.tpj.packinglist.repository.UserRepository;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Api(description = "Users management API")
@RequestMapping("api")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserRepository userRepo;

    @RequestMapping(value = "/users", method = RequestMethod.GET)
    public @ResponseBody List<User> usersList() {
        logger.debug("get users list");
        return userRepo.findAll();
    }

    @RequestMapping(value = "/users/{userId}", method = RequestMethod.GET)
    public @ResponseBody User getUser(@PathVariable Long userId) {
        logger.debug("get user");
        return userRepo.findOne(userId);
    }

    @RequestMapping(value = "/users", method = RequestMethod.POST)
    public @ResponseBody User saveUser(@RequestBody User user) {
        logger.debug("save user");
        userRepo.save(user);
        return user;
    }


}

 

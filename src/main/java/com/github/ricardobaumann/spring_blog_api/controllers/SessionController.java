package com.github.ricardobaumann.spring_blog_api.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

/**
 * Created by ricardobaumann on 03/11/16.
 */
@RestController
public class SessionController {

    @RequestMapping(value = "/user")
    public Principal getUser(Principal user) {
        return user;
    }

}

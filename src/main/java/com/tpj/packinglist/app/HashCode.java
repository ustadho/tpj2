package com.tpj.packinglist.app;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 *
 * @author faheem
 */
public class HashCode {

    public String getHashPassword(String password) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String hashedPassword = passwordEncoder.encode(password);

        System.out.println(hashedPassword);
        return hashedPassword;
    }
}

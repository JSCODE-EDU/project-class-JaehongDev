package com.jaehong.projectclassjaehongdev.global.config;

import org.mindrot.jbcrypt.BCrypt;

public class BcryptPasswordEncode implements PasswordEncode {
    private static final String SALT = BCrypt.gensalt();


    @Override
    public String encode(String password) {
        return BCrypt.hashpw(password, SALT);
    }

    @Override
    public boolean compare(String password, String encodedPassword) {
        return BCrypt.checkpw(password, encodedPassword);
    }

}

package com.jaehong.projectclassjaehongdev.global.config;


public interface PasswordEncode {

    String encode(String password);

    boolean compare(String password, String encodedPassword);
}

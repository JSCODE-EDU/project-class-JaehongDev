package com.jaehong.projectclassjaehongdev.jwt;

public interface TokenService {
    String issuedToken(Object subject, long periodSecond);


    Long getById(final String token);


    boolean verifyToken(final String token);


}

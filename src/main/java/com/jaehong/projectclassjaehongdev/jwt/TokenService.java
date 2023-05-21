package com.jaehong.projectclassjaehongdev.jwt;

public interface TokenService {
    String issuedToken(Object subject, final long periodSecond);

    String getSubject(final String token);

    boolean verifyToken(final String token);


}

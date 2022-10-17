package com.company.project.jwt;

import org.springframework.stereotype.Component;

@Component
public class JwtConfigProperties {
    private int validity = 1000 * 60 * 60 * 5;
    private byte[] secret = "JWT".getBytes();

    public int getValidity() {
        return validity;
    }

    public void setValidity(int validity) {
        this.validity = validity;
    }

    public byte[] getSecret() {
        return secret;
    }

    public void setSecret(byte[] secret) {
        this.secret = secret;
    }
}

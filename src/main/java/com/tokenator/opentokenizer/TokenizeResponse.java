package com.tokenator.opentokenizer;


public class TokenizeResponse {
    private String surrogatePan;

    public TokenizeResponse(String surrogatePan) {
        this.setSurrogatePan(surrogatePan);
    }

    public String getSurrogatePan() {
        return surrogatePan;
    }

    public void setSurrogatePan(String surrogatePan) {
        this.surrogatePan = surrogatePan;
    }
}

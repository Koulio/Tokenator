package com.tokenator.opentokenizer;


public class DetokenizeResponse {
    private String pan;

    public DetokenizeResponse(String pan) {
        this.setPan(pan);
    }

    public String getPan() {
        return pan;
    }

    public void setPan(String pan) {
        this.pan = pan;
    }
}

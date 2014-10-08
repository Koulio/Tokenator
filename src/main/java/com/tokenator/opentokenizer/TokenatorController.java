package com.tokenator.opentokenizer;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TokenatorController {

    @RequestMapping("/tokenize")
    public TokenizeResponse tokenize(@RequestParam(value="pan") String pan) {
        return new TokenizeResponse(rotatePanDigits(pan, 1));
    }

    @RequestMapping("/detokenize")
    public DetokenizeResponse detokenize(@RequestParam(value="pan") String pan) {
        return new DetokenizeResponse(rotatePanDigits(pan, 9));
    }


    private String rotatePanDigits(String pan, int rotateDigitsBy) {
        byte[] panBytes = pan.getBytes();

        for (int i = 0; i < panBytes.length; i++) {
            byte digit = panBytes[i];
            if (digit >= '0' && digit <= '9') {
                panBytes[i] = (byte) (((digit - '0' + rotateDigitsBy) % 10) + '0');
            }
        }

        return new String(panBytes);
    }

}

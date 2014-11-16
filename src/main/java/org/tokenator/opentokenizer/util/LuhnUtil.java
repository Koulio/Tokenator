package org.tokenator.opentokenizer.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tokenator.opentokenizer.InvalidAccountNumberException;



public class LuhnUtil {

    private static final Logger log = LoggerFactory.getLogger(LuhnUtil.class);

   /*
    *  Validates the the length and Luhn checksum of the account number.  If it is
    *  invalid, an InvalidAccountNumberException is thrown.
    *
    *  If the account number has a single 'L' in it (in any position!), we replace the
    *  'L' character by the digit that will give the entire account number a valid Luhn
    *  checksum.
    */
    public static String validateAcctNumAndAdjustLuhn(String accountNum) {
        int numDigits = accountNum.length();

        if (numDigits < 12) {
            throw new InvalidAccountNumberException("Account number must be at least 12 digits");
        }

        if (numDigits > 19) {
            throw new InvalidAccountNumberException("Account number exceeds 19 digits");
        }

        byte[] accountNumBytes = accountNum.getBytes();
        int replacementPos = -1;
        boolean replacementPosIsEven = false;
        int sum = 0;

        boolean isEvenPos = false; // from right hand side

        for(int pos = numDigits - 1; pos >= 0; pos--, isEvenPos = !isEvenPos) {
            byte asciiDigit = accountNumBytes[pos];

            if (asciiDigit == 'L' || asciiDigit == 'X') {
                if (replacementPos != -1) {
                    throw new InvalidAccountNumberException("Account number has multiple Luhn placeholders");
                }
                replacementPos = pos;
                replacementPosIsEven = isEvenPos;
                continue; // sum += 0
            }

            if (asciiDigit < '0' || asciiDigit > '9') {
                throw new InvalidAccountNumberException("Account number has invalid character");
            }

            int digit = asciiDigit - '0';
            if (isEvenPos) {
                digit *= 2;
                if (digit > 9) {
                    digit -= 9;
                }
            }

            sum += digit;
        }


        int remainder = sum % 10;

        if (replacementPos != -1) {
            int replacementValue = (remainder > 0) ? 10 - remainder : 0;
            if (replacementPosIsEven) {
                if (replacementValue % 2 != 0) {
                    replacementValue += 9;
                }
                replacementValue /= 2;
            }
            accountNumBytes[replacementPos] = (byte) ('0' + replacementValue);
        } else if (remainder != 0) {
            throw new InvalidAccountNumberException("Luhn checksum failed");
        }

        return new String(accountNumBytes);
    }
}

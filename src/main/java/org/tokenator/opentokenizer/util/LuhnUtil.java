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
            throw new InvalidAccountNumberException("PAN must be at least 12 digits");
        } else if (numDigits > 19) {
            throw new InvalidAccountNumberException("PAN exceeds 19 digits");
        }

        byte[] accountNumBytes = accountNum.getBytes();
        int replacementPos = -1;
        boolean isEvenPos = false; // is even starting from right
        boolean replacementPosIsEven = false;
        int sum = 0;

        for(int pos = numDigits - 1; pos >= 0; pos--) {
            byte b = accountNumBytes[pos];
            if (b >= '0' && b <= '9') {
                int digit = b - '0';
                if (isEvenPos) {
                    digit *= 2;
                    if (digit > 9) {
                        digit -= 9;
                    }
                }
                sum += digit;
            } else if (b == 'L' || b == 'X') {
                if (replacementPos != -1) {
                    throw new InvalidAccountNumberException("PAN can only have one Luhn placeholder");
                }
                replacementPos = pos;
                replacementPosIsEven = isEvenPos;
            } else {
                throw new InvalidAccountNumberException("PAN has invalid character");
            }

            isEvenPos = !isEvenPos;
        }

        if (sum == 0) {
            throw new InvalidAccountNumberException("Luhn checksum is zero");
        }

        int remainder = sum % 10;

        if (replacementPos != -1) {
            int placeholderValue = (remainder > 0) ? 10 - remainder : 0;
            if (replacementPosIsEven) {
                if (placeholderValue % 2 != 0) {
                    // remainder was odd, add 9 to make it even before dividing by 2
                    placeholderValue += 9;
                }
                placeholderValue /= 2;
            }
            accountNumBytes[replacementPos] = (byte) ('0' + placeholderValue);
        } else if (remainder != 0) {
            throw new InvalidAccountNumberException("Luhn check failed, place an 'L' in the account number for auto calculation");
        }

        return new String(accountNumBytes);
    }
}

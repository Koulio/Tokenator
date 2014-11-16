package org.tokenator.opentokenizer.util;

import org.junit.Test;
import org.tokenator.opentokenizer.InvalidAccountNumberException;

import static org.junit.Assert.assertEquals;

public class LuhnUtilTest {

    @Test
    public void testValidateAcctNumAndAdjustLuhn_validLuhn() throws Exception {
        String acccountNumWithValidLuhn = "4563960122001999";
        LuhnUtil.validateAcctNumAndAdjustLuhn(acccountNumWithValidLuhn);
    }

    @Test(expected = InvalidAccountNumberException.class)
    public void testValidateAcctNumAndAdjustLuhn_invalidLuhn() throws Exception {
        String accountNumWithValidLuhn = "4563960122001990";
        LuhnUtil.validateAcctNumAndAdjustLuhn(accountNumWithValidLuhn);
    }

    @Test
    public void testValidateAcctNumAndAdjustLuhn_luhnReplacementWithOddPos() throws Exception {
        String accountNumWithValidLuhn       = "4563960122001999";
        String accountNumWithLuhnPlaceholder = "456396012200199L";
        String result = LuhnUtil.validateAcctNumAndAdjustLuhn(accountNumWithLuhnPlaceholder);
        assertEquals(accountNumWithValidLuhn, result);
    }


    @Test
    public void testValidateAcctNumAndAdjustLuhn_luhnReplacementWithOddPosAndZeroValue() throws Exception {
        String accountNumWithValidLuhn       = "36262861854440";
        String accountNumWithLuhnPlaceholder = "3626286185444L";
        String result = LuhnUtil.validateAcctNumAndAdjustLuhn(accountNumWithLuhnPlaceholder);
        assertEquals(accountNumWithValidLuhn, result);
    }

    @Test
    public void testValidateAcctNumAndAdjustLuhn_ReplacementWithEvenPosAndEvenValue() throws Exception {
        String accountNumWithValidLuhn       = "6011674757511148";
        String accountNumWithLuhnPlaceholder = "60116747575111L8";
        String result = LuhnUtil.validateAcctNumAndAdjustLuhn(accountNumWithLuhnPlaceholder);
        assertEquals(accountNumWithValidLuhn, result);
    }

    @Test
    public void testValidateAcctNumAndAdjustLuhn_ReplacementWithEvenPosAndZeroValue() throws Exception {
        String accountNumWithValidLuhn       = "4026033684942306";
        String accountNumWithLuhnPlaceholder = "40260336849423L6";
        String result = LuhnUtil.validateAcctNumAndAdjustLuhn(accountNumWithLuhnPlaceholder);
        assertEquals(accountNumWithValidLuhn, result);
    }


    @Test
    public void testValidateAcctNumAndAdjustLuhn_ReplacementWithEvenPosAndOddValue() throws Exception {
        String accountNumWithValidLuhn       = "345739373393336";
        String accountNumWithLuhnPlaceholder = "3457393733933L6";
        String result = LuhnUtil.validateAcctNumAndAdjustLuhn(accountNumWithLuhnPlaceholder);
        assertEquals(accountNumWithValidLuhn, result);
    }

    @Test
    public void testValidateAcctNumAndAdjustLuhn_luhnPlaceholderOdd() throws Exception {
        String accountNumWithValidLuhn = "4563960122001999";
        String accountNumberWithLuhnPlaceholder = "456396012200199L";
        String result = LuhnUtil.validateAcctNumAndAdjustLuhn(accountNumberWithLuhnPlaceholder);
        assertEquals(accountNumWithValidLuhn, result);
    }


    @Test
    public void testValidateAllPositions1() throws Exception {
        String expectedAccountNum = "5407789370224905";
        String[] allLuhnReplacements = {
                "540778937022490L",
                "54077893702249L5",
                "5407789370224L05",
                "540778937022L905",
                "54077893702L4905",
                "5407789370L24905",
                "540778937L224905",
                "54077893L0224905",
                "5407789L70224905",
                "540778L370224905",
                "54077L9370224905",
                "5407L89370224905",
                "540L789370224905",
                "54L7789370224905",
                "5L07789370224905",
                "L407789370224905",
        };

        for (String luhnReplacement : allLuhnReplacements) {
            String replacedResult = LuhnUtil.validateAcctNumAndAdjustLuhn(luhnReplacement);
            assertEquals(expectedAccountNum, replacedResult);
        }
    }

    @Test
    public void testValidateAllPositions2() {
        String expectedAccountNum = "6763431195496147";
        String[] allLuhnReplacements = {
                "676343119549614L",
                "67634311954961L7",
                "6763431195496L47",
                "676343119549L147",
                "67634311954L6147",
                "6763431195L96147",
                "676343119L496147",
                "67634311L5496147",
                "6763431L95496147",
                "676343L195496147",
                "67634L1195496147",
                "6763L31195496147",
                "676L431195496147",
                "67L3431195496147",
                "6L63431195496147",
                "L763431195496147",
        };

        for (String luhnReplacement : allLuhnReplacements) {
            String replacedResult = LuhnUtil.validateAcctNumAndAdjustLuhn(luhnReplacement);
            assertEquals(expectedAccountNum, replacedResult);
        }
    }
}
package org.tokenator.opentokenizer.util;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class DateSerializer extends JsonSerializer<Date> {

    public static final String DATE_FORMAT = "yyMM";
    public static TimeZone UTC = TimeZone.getTimeZone("UTC");

    private static SimpleDateFormat getFormatter() {
        // SimpleDateFormat isn't thread safe, so we create it here:
        SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT);
        format.setTimeZone(UTC);
        return  format;
    }


    public static String convert(Date date) {
        return getFormatter().format(date);
    }

    public static Date convert(String dateString) throws ParseException {
        return getFormatter().parse(dateString);
    }

    @Override
    public void serialize(Date value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeString(getFormatter().format(value));
    }
}
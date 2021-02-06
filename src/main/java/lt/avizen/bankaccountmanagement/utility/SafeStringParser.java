package lt.avizen.bankaccountmanagement.utility;

import org.apache.commons.lang3.time.DateUtils;

import java.text.ParseException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeParseException;
import java.util.Date;

public final class SafeStringParser {
    
    /// TODO: Support more formats or change the approach.
    public static Date parseDate(String stringDate) {
        try {
            return DateUtils.parseDate(stringDate, "dd-MM-yyyy HH:mm:ss", "dd-MM-yyyy HH:mm:ss.S", "dd/MM/yyyy HH:mm");
        } catch (ParseException e) {
            try {
                LocalDateTime localDateTime = LocalDateTime.parse(stringDate);
                Instant instant = localDateTime.toInstant(ZoneOffset.UTC);
                return Date.from(instant);
            } catch (DateTimeParseException ex) {
                return null;
            }
        }
    }

    public static Double parseDouble(String stringDouble) {
        try {
            return Double.parseDouble(stringDouble);
        } catch (NullPointerException | NumberFormatException ex) {
            return null;
        }
    }
}

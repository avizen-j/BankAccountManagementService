package lt.avizen.bankaccountmanagement.utility;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public final class SafeStringParser {

    public static LocalDateTime parseDateTime(String stringDate) {
        try {
            return LocalDateTime.parse(stringDate);
        } catch (DateTimeParseException ex) {
            return null;
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

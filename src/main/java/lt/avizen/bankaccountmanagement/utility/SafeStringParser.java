package lt.avizen.bankaccountmanagement.utility;

import org.apache.commons.lang3.time.DateUtils;

import java.text.ParseException;
import java.util.Date;

public final class SafeStringParser {
    public static Date parseDate(String stringDate)
    {
        try
        {
            /// TODO: Support more formats or change the approach.
            return DateUtils.parseDate(stringDate, "dd-MM-yyyy HH:mm:ss", "dd/MM/yyyy HH:mm");
        } catch (ParseException e) {
            return null;
        }
    }

    public static Double parseDouble(String stringDouble)
    {
        try
        {
            return Double.parseDouble(stringDouble);
        } catch (NullPointerException | NumberFormatException ex) {
            return null;
        }
    }
}

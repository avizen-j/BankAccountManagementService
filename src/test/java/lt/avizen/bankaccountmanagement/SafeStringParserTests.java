package lt.avizen.bankaccountmanagement;

import lt.avizen.bankaccountmanagement.utility.SafeStringParser;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import java.time.LocalDateTime;

public class SafeStringParserTests {

    @Test
    public void parseDateTime_CorrectFormat_ParsesAsDateTime(){
        // Arrange
        String dateTimeString = "2020-01-12T12:01:05";
        String localDateTimeString = "2020-01-12T12:01:05";

        // Act
        LocalDateTime localDateTime = SafeStringParser.parseDateTime(dateTimeString);

        // Assert
        Assertions.assertEquals(localDateTime.toString(), localDateTimeString);
    }

    @Test
    public void parseDateTime_IncorrectFormat_ReturnsNull() {
        // Arrange
        String dateTimeString = "2020-13-12";

        // Act
        LocalDateTime localDateTime = SafeStringParser.parseDateTime(dateTimeString);

        // Assert
        Assertions.assertNull(localDateTime);
    }

    @Test
    public void parseDateTime_IncorrectValue_ReturnsNull() {
        // Arrange
        String dateTimeString = "2020-13-12 12:01:05";

        // Act
        LocalDateTime localDateTime = SafeStringParser.parseDateTime(dateTimeString);

        // Assert
        Assertions.assertNull(localDateTime);
    }

    @Test
    public void parseDouble_CorrectDoubleString_ReturnsDouble() {
        // Arrange
        String doubleString = "2020.23";

        // Act
        Double value = SafeStringParser.parseDouble(doubleString);

        // Assert
        Assertions.assertEquals(2020.23, value);
    }

    @Test
    public void parseDouble_IncorrectValue_ReturnsDouble() {
        // Arrange
        String doubleString = "xxxxxxxx";

        // Act
        Double value = SafeStringParser.parseDouble(doubleString);

        // Assert
        Assertions.assertNull(value);
    }


}

package lt.avizen.bankaccountmanagement;

import lt.avizen.bankaccountmanagement.domain.BankStatement;
import lt.avizen.bankaccountmanagement.enums.BankStatementEnum;
import lt.avizen.bankaccountmanagement.service.CsvService;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CsvServiceTests {

    private Path csvFilesDirectory;

    @Autowired
    private CsvService csvService;

    @Before
    public void setup() {
        csvFilesDirectory = Path.of("src", "test", "resources");
    }

    @Test
    public void writeItemsToCsv_NotEmptyList_FormsCorrectCsv() throws IOException {
        // Arrange
        List<BankStatement> bankStatements = new ArrayList<>();
        BankStatement bankStatement1 = BankStatement.builder()
                .accountNumber("LT9111111")
                .operationDate(LocalDateTime.now())
                .beneficiary("Colson")
                .comment("")
                .amount(579.99)
                .currency("EUR")
                .build();
        BankStatement bankStatement2 = BankStatement.builder()
                .accountNumber("LT91133311")
                .operationDate(LocalDateTime.now())
                .beneficiary("Pete")
                .comment("No comments.")
                .amount(123d)
                .currency("USD")
                .build();
        bankStatements.add(bankStatement1);
        bankStatements.add(bankStatement2);

        // Act
        byte[] csvByteArray = csvService.writeItemsToCsv(bankStatements);

        String csvString = new String(csvByteArray, StandardCharsets.UTF_8);
        StringBuilder documentWithHeader = new StringBuilder(Arrays.stream(BankStatementEnum.values()).map(Enum::toString).collect(Collectors.joining(",")))
                .append(System.lineSeparator());
        bankStatements.forEach(
                s -> documentWithHeader
                        .append(s.getAccountNumber())
                        .append(",")
                        .append(s.getOperationDate())
                        .append(",")
                        .append(s.getBeneficiary())
                        .append(",")
                        .append(s.getComment())
                        .append(",")
                        .append(s.getAmount())
                        .append(",")
                        .append(s.getCurrency())
                        .append(System.lineSeparator()));

        // Assert
        Assertions.assertEquals(documentWithHeader.toString(), csvString);
    }

    @Test
    public void writeItemsToCsv_EmptyList_WritesHeaderOnly() throws IOException {
        // Arrange
        List<BankStatement> bankStatements = new ArrayList<>();

        // Act
        byte[] csvByteArray = csvService.writeItemsToCsv(bankStatements);

        String csvString = new String(csvByteArray, StandardCharsets.UTF_8);
        StringBuilder documentWithHeader = new StringBuilder(Arrays.stream(BankStatementEnum.values()).map(Enum::toString).collect(Collectors.joining(",")))
                .append(System.lineSeparator());

        // Assert
        Assertions.assertEquals(documentWithHeader.toString(), csvString);
    }

    @Test
    public void readCsvToItems_ValidDocument_FormsValidItemList() throws IOException {
        // Arrange
        Path file = csvFilesDirectory.resolve("ValidBankStatements.csv");
        byte[] content = Files.readAllBytes(file);

        // Act
        List<BankStatement> bankStatements = csvService.readCsvToItems(content);

        // Assert
        Assertions.assertEquals(19, bankStatements.size());
        Assertions.assertEquals("132255618-0", bankStatements.stream()
                .findFirst()
                .get()
                .getAccountNumber());
        Assertions.assertEquals("051382860-5", bankStatements.stream()
                .skip(bankStatements.size() - 1)
                .findFirst()
                .get()
                .getAccountNumber());
    }

    @Test
    public void readCsvToItems_DocumentContainingInvalidFormatValues_FormsValidItemListChangingErroneousValuesToNull() throws IOException {
        // Arrange
        Path file = csvFilesDirectory.resolve("DocumentWithInvalidFormatValues.csv");
        byte[] content = Files.readAllBytes(file);

        // Act
        List<BankStatement> bankStatements = csvService.readCsvToItems(content);

        // Assert
        Assertions.assertEquals(5, bankStatements.size());
        Assertions.assertTrue(bankStatements.get(0).getAccountNumber().equals("132255618-0") &&
                bankStatements.get(0).getOperationDate() == null &&
                bankStatements.get(0).getBeneficiary().equals("Nevin") &&
                bankStatements.get(0).getComment().equals("Configurable 3rd generation portal") &&
                bankStatements.get(0).getAmount() == 113.4 &&
                bankStatements.get(0).getCurrency().equals("IDR"));
        Assertions.assertTrue(bankStatements.get(1).getAccountNumber().equals("209825177-7") &&
                bankStatements.get(1).getOperationDate() == null);
        Assertions.assertTrue(bankStatements.get(4).getAccountNumber().equals("820053380-8") &&
                bankStatements.get(4).getAmount() == null);
    }

    @Test
    public void readCsvToItems_EmptyDocument_FormsEmptyList() throws IOException {
        // Arrange
        Path file = csvFilesDirectory.resolve("EmptyBankStatements.csv");
        byte[] content = Files.readAllBytes(file);

        // Act
        List<BankStatement> bankStatements = csvService.readCsvToItems(content);

        // Assert
        Assertions.assertEquals(0, bankStatements.size());
    }

    @Test
    public void readCsvToItems_HeaderWithoutData_FormsEmptyList() throws IOException {
        // Arrange
        Path file = csvFilesDirectory.resolve("HeaderOnlyBankStatements.csv");
        byte[] content = Files.readAllBytes(file);

        // Act
        List<BankStatement> bankStatements = csvService.readCsvToItems(content);

        // Assert
        Assertions.assertEquals(0, bankStatements.size());
    }
}

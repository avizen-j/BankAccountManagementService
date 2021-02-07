package lt.avizen.bankaccountmanagement;

import lt.avizen.bankaccountmanagement.domain.BankStatement;
import lt.avizen.bankaccountmanagement.model.ValidationResult;
import lt.avizen.bankaccountmanagement.repository.BankStatementRepository;
import lt.avizen.bankaccountmanagement.service.CsvService;
import lt.avizen.bankaccountmanagement.service.ManagementServiceImpl;
import lt.avizen.bankaccountmanagement.service.ValidationService;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.springframework.data.jpa.domain.Specification;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@RunWith(MockitoJUnitRunner.class)
public class ManagementServiceTests {

    @Mock
    private Logger logger;

    @Mock
    private BankStatementRepository repository;

    @Mock
    private ValidationService validator;

    @Mock
    private CsvService csvService;

    @InjectMocks
    private ManagementServiceImpl managementService;

    @Test
    public void uploadBankStatementCsv_ValidCsvByteArray_CallsSaveAll() throws IOException {
        // Arrange
        byte[] csvByteArray = "test".getBytes();
        List<BankStatement> bankStatements = new ArrayList<BankStatement>();
        BankStatement bankStatement = BankStatement.builder()
                .accountNumber("LT9111111")
                .operationDate(LocalDateTime.now())
                .beneficiary("Colson")
                .comment(null)
                .amount(579.99)
                .currency("EUR")
                .build();
        bankStatements.add(bankStatement);
        when(csvService.readCsvToItems(csvByteArray)).thenReturn(bankStatements);
        when(validator.validateList(bankStatements)).thenReturn(
                new ValidationResult<>(1, 0, bankStatements, new ArrayList<>()));

        // Act
        managementService.uploadBankStatementCsv(csvByteArray);

        // Assert
        Mockito.verify(repository).saveAll(bankStatements);
    }

    @Test
    public void uploadBankStatementCsv_ValidWithMistakesCsvByteArray_CallsSaveAllWithValidRecords() throws IOException {
        // Arrange
        byte[] csvByteArray = "test".getBytes();
        List<BankStatement> allBankStatements = new ArrayList<>();
        List<BankStatement> validBankStatements = new ArrayList<>();
        BankStatement bankStatement1 = BankStatement.builder()
                .accountNumber("LT9111111")
                .operationDate(LocalDateTime.now())
                .beneficiary("Colson")
                .comment(null)
                .amount(579.99)
                .currency("EUR")
                .build();
        BankStatement bankStatement2 = BankStatement.builder()
                .accountNumber("LT91144441")
                .operationDate(LocalDateTime.now())
                .beneficiary("Sarah")
                .comment("cmnt")
                .amount(null)
                .currency("EUR")
                .build();
        allBankStatements.add(bankStatement1);
        allBankStatements.add(bankStatement2);
        validBankStatements.add(bankStatement1);
        when(csvService.readCsvToItems(csvByteArray)).thenReturn(allBankStatements);
        when(validator.validateList(allBankStatements)).thenReturn(
                new ValidationResult<>(1, 1, validBankStatements, new ArrayList<>()));

        // Act
        managementService.uploadBankStatementCsv(csvByteArray);

        // Assert
        Mockito.verify(repository).saveAll(validBankStatements);
    }

    @Test
    public void uploadBankStatementCsv_InvalidCsvByteArray_DoesNotCallSaveAll() throws IOException {
        // Arrange
        byte[] csvByteArray = "test".getBytes();
        List<BankStatement> allBankStatements = new ArrayList<>();
        BankStatement bankStatement = BankStatement.builder()
                .accountNumber("LT9111111")
                .operationDate(LocalDateTime.now())
                .beneficiary("Colson")
                .comment(null)
                .amount(579.99)
                .currency("EUR")
                .build();
        allBankStatements.add(bankStatement);
        when(csvService.readCsvToItems(csvByteArray)).thenReturn(allBankStatements);
        when(validator.validateList(allBankStatements)).thenReturn(
                new ValidationResult<>(0, 1, new ArrayList<>(), new ArrayList<>()));

        // Act
        managementService.uploadBankStatementCsv(csvByteArray);

        // Assert
        Mockito.verify(repository, never()).saveAll(any());
    }

    @Test
    public void exportBankStatementCsv_RecordsFound_CallsCsvService() throws IOException {
        // Arrange
        List<BankStatement> bankStatements = new ArrayList<BankStatement>();
        BankStatement bankStatement = BankStatement.builder()
                .accountNumber("LT9111111")
                .operationDate(LocalDateTime.now())
                .beneficiary("Colson")
                .comment(null)
                .amount(579.99)
                .currency("EUR")
                .build();
        bankStatements.add(bankStatement);
        when(repository.findAll(ArgumentMatchers.<Specification<BankStatement>>any())).thenReturn(bankStatements);

        // Act
        managementService.exportBankStatementCsv(LocalDate.now(), LocalDate.now().plusDays(2));

        // Assert
        Mockito.verify(csvService).writeItemsToCsv(bankStatements);
    }

    @Test
    public void exportBankStatementCsv_NoRecordsFound_ThrowsException() {
        // Arrange
        when(repository.findAll(ArgumentMatchers.<Specification<BankStatement>>any())).thenReturn(new ArrayList<BankStatement>());

        // Act
        Executable executable = () -> managementService.exportBankStatementCsv(LocalDate.now(), LocalDate.now().plusDays(2));

        // Assert
        Throwable exception = assertThrows(NoSuchElementException.class, executable);
        assertEquals("No records were found", exception.getMessage());
    }

    @Test
    public void calculateAccountBalance_RecordsFound_CalculatesSum() {
        // Arrange
        List<BankStatement> bankStatements = new ArrayList<BankStatement>();
        BankStatement bankStatement1 = BankStatement.builder()
                .accountNumber("LT9111111")
                .operationDate(LocalDateTime.now())
                .beneficiary("Colson")
                .comment(null)
                .amount(120.99)
                .currency("EUR")
                .build();
        BankStatement bankStatement2 = BankStatement.builder()
                .accountNumber("LT9111111")
                .operationDate(LocalDateTime.now())
                .beneficiary("Colson")
                .comment(null)
                .amount(12.99)
                .currency("EUR")
                .build();
        bankStatements.add(bankStatement1);
        bankStatements.add(bankStatement2);
        when(repository.findAll(ArgumentMatchers.<Specification<BankStatement>>any())).thenReturn(bankStatements);

        // Act
        double accountBalance = managementService.calculateAccountBalance("LT9111111", LocalDate.now(), LocalDate.now().plusDays(2));

        // Assert
        Assertions.assertEquals(120.99 + 12.99, accountBalance);
    }

    @Test
    public void calculateAccountBalance_NoRecordsFound_ThrowsException() {
        // Arrange
        when(repository.findAll(ArgumentMatchers.<Specification<BankStatement>>any())).thenReturn(new ArrayList<BankStatement>());

        // Act
        Executable executable = () -> managementService.calculateAccountBalance("LT9111111", LocalDate.now(), LocalDate.now().plusDays(2));

        // Assert
        Throwable exception = assertThrows(NoSuchElementException.class, executable);
        assertEquals("No records for 'LT9111111' account were found", exception.getMessage());
    }
}

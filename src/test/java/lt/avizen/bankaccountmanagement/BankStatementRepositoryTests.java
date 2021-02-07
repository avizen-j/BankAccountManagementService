package lt.avizen.bankaccountmanagement;

import lt.avizen.bankaccountmanagement.domain.BankStatement;
import lt.avizen.bankaccountmanagement.repository.BankStatementRepository;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.junit4.SpringRunner;

import javax.validation.ConstraintViolationException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static lt.avizen.bankaccountmanagement.domain.BankStatementSpecification.getRecordsByDate;
import static lt.avizen.bankaccountmanagement.domain.BankStatementSpecification.isEqual;

@RunWith(SpringRunner.class)
@DataJpaTest
public class BankStatementRepositoryTests {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private BankStatementRepository repository;

    @Test
    public void saveAll_AllFieldsFilled_SavesRecord() {
        // Arrange
        List<BankStatement> bankStatements = new ArrayList<BankStatement>();
        BankStatement bankStatement = BankStatement.builder()
                .accountNumber("LT9111111")
                .operationDate(LocalDateTime.now())
                .beneficiary("Colson")
                .comment("for a new guitar")
                .amount(579.99)
                .currency("EUR")
                .build();
        bankStatements.add(bankStatement);

        // Act
        repository.saveAll(bankStatements);
        repository.flush();
        BankStatement actualBankStatement = this.entityManager.find(BankStatement.class, bankStatement.getId());

        // Assert
        Assertions.assertNotNull(actualBankStatement);
        Assertions.assertEquals(bankStatement.getAccountNumber(), actualBankStatement.getAccountNumber());
        Assertions.assertEquals(bankStatement.getOperationDate(), actualBankStatement.getOperationDate());
        Assertions.assertEquals(bankStatement.getBeneficiary(), actualBankStatement.getBeneficiary());
        Assertions.assertEquals(bankStatement.getComment(), actualBankStatement.getComment());
        Assertions.assertEquals(bankStatement.getAmount(), actualBankStatement.getAmount());
        Assertions.assertEquals(bankStatement.getCurrency(), actualBankStatement.getCurrency());
    }

    @Test
    public void saveAll_OptionalFieldIsNull_SavesRecord() {
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

        // Act
        repository.saveAll(bankStatements);
        repository.flush();
        BankStatement actualBankStatement = entityManager.find(BankStatement.class, bankStatement.getId());

        // Assert
        Assertions.assertNotNull(actualBankStatement);
        Assertions.assertEquals(bankStatement.getAccountNumber(), actualBankStatement.getAccountNumber());
        Assertions.assertEquals(bankStatement.getOperationDate(), actualBankStatement.getOperationDate());
        Assertions.assertEquals(bankStatement.getBeneficiary(), actualBankStatement.getBeneficiary());
        Assertions.assertNull(actualBankStatement.getComment());
        Assertions.assertEquals(bankStatement.getAmount(), actualBankStatement.getAmount());
        Assertions.assertEquals(bankStatement.getCurrency(), actualBankStatement.getCurrency());
    }

    @Test(expected = ConstraintViolationException.class)
    public void saveAll_RequiredFieldIsNull_ThrowsException() {
        // Arrange
        List<BankStatement> bankStatements = new ArrayList<BankStatement>();
        BankStatement bankStatement = BankStatement.builder()
                .accountNumber(null)
                .operationDate(LocalDateTime.now())
                .beneficiary("Colson")
                .comment("for a new guitar")
                .amount(579.99)
                .currency("EUR")
                .build();
        bankStatements.add(bankStatement);

        // Act
        repository.saveAll(bankStatements);
        repository.flush();
    }

    @Test(expected = ConstraintViolationException.class)
    public void saveAll_RequiredFieldIsEmpty_ThrowsException() {
        // Arrange
        List<BankStatement> bankStatements = new ArrayList<BankStatement>();
        BankStatement bankStatement = BankStatement.builder()
                .accountNumber("")
                .operationDate(LocalDateTime.now())
                .beneficiary("Colson")
                .comment("for a new guitar")
                .amount(579.99)
                .currency("EUR")
                .build();
        bankStatements.add(bankStatement);

        // Act
        repository.saveAll(bankStatements);
        repository.flush();
    }

    @Test
    public void findAll_OnlyFromDateIsPassed_FindsAllGreaterThanOrEqualToDate() {
        // Arrange
        BankStatement bankStatement = BankStatement.builder()
                .accountNumber("LT9111111")
                .operationDate(LocalDateTime.now())
                .beneficiary("Colson")
                .comment(null)
                .amount(579.99)
                .currency("EUR")
                .build();
        BankStatement bankStatement2 = BankStatement.builder()
                .accountNumber("LT1211121")
                .operationDate(LocalDateTime.now().plusDays(2))
                .beneficiary("John")
                .comment(null)
                .amount(111.99)
                .currency("USD")
                .build();
        BankStatement bankStatement3 = BankStatement.builder()
                .accountNumber("LT1214444")
                .operationDate(LocalDateTime.now().plusDays(3).plusHours(3))
                .beneficiary("Sarah")
                .comment(null)
                .amount(87d)
                .currency("USD")
                .build();
        entityManager.persistAndFlush(bankStatement);
        entityManager.persistAndFlush(bankStatement2);
        entityManager.persistAndFlush(bankStatement3);
        Specification<BankStatement> specification = Specification.where(null);
        specification = getRecordsByDate(LocalDate.now().minusDays(1), null, specification);

        // Act
        List<BankStatement> actualBankStatements = repository.findAll(specification);

        // Assert
        Assertions.assertEquals(3, actualBankStatements.size());
    }

    @Test
    public void findAll_OnlyToDateIsPassed_FindsAllLessThanOrEqualToDate() {
        // Arrange
        BankStatement bankStatement = BankStatement.builder()
                .accountNumber("LT9111111")
                .operationDate(LocalDateTime.now())
                .beneficiary("Colson")
                .comment(null)
                .amount(579.99)
                .currency("EUR")
                .build();
        BankStatement bankStatement2 = BankStatement.builder()
                .accountNumber("LT1211121")
                .operationDate(LocalDateTime.now().plusDays(2))
                .beneficiary("John")
                .comment(null)
                .amount(111.99)
                .currency("USD")
                .build();
        BankStatement bankStatement3 = BankStatement.builder()
                .accountNumber("LT1214444")
                .operationDate(LocalDateTime.now().plusDays(3).plusHours(3))
                .beneficiary("Sarah")
                .comment(null)
                .amount(87d)
                .currency("USD")
                .build();
        entityManager.persistAndFlush(bankStatement);
        entityManager.persistAndFlush(bankStatement2);
        entityManager.persistAndFlush(bankStatement3);
        Specification<BankStatement> specification = Specification.where(null);
        specification = getRecordsByDate(null, LocalDate.now().plusDays(3), specification);

        // Act
        List<BankStatement> actualBankStatements = repository.findAll(specification);

        // Assert
        Assertions.assertEquals(2, actualBankStatements.size());
    }

    @Test
    public void findAll_FromToDatesPassed_FindsAllBetweenDates() {
        // Arrange
        BankStatement bankStatement = BankStatement.builder()
                .accountNumber("LT9111111")
                .operationDate(LocalDateTime.now())
                .beneficiary("Colson")
                .comment(null)
                .amount(579.99)
                .currency("EUR")
                .build();
        BankStatement bankStatement2 = BankStatement.builder()
                .accountNumber("LT1211121")
                .operationDate(LocalDateTime.now().plusDays(2))
                .beneficiary("John")
                .comment(null)
                .amount(111.99)
                .currency("USD")
                .build();
        BankStatement bankStatement3 = BankStatement.builder()
                .accountNumber("LT1214444")
                .operationDate(LocalDateTime.now().plusDays(3).plusHours(3))
                .beneficiary("Sarah")
                .comment(null)
                .amount(87d)
                .currency("USD")
                .build();
        entityManager.persistAndFlush(bankStatement);
        entityManager.persistAndFlush(bankStatement2);
        entityManager.persistAndFlush(bankStatement3);
        Specification<BankStatement> specification = Specification.where(null);
        specification = getRecordsByDate(LocalDate.now().plusDays(1), LocalDate.now().plusDays(3), specification);

        // Act
        List<BankStatement> actualBankStatements = repository.findAll(specification);

        // Assert
        Assertions.assertEquals(1, actualBankStatements.size());
        Assertions.assertEquals(bankStatement2.getAccountNumber(), actualBankStatements.get(0).getAccountNumber());
        Assertions.assertEquals(bankStatement2.getOperationDate(), actualBankStatements.get(0).getOperationDate());
    }

    @Test
    public void findAll_FromToDatesAndAccountNumberPassed_FindsAllBetweenDatesForAccountNumber() {
        // Arrange
        BankStatement bankStatement = BankStatement.builder()
                .accountNumber("LT9111111")
                .operationDate(LocalDateTime.now())
                .beneficiary("Colson")
                .comment(null)
                .amount(579.99)
                .currency("EUR")
                .build();
        BankStatement bankStatement2 = BankStatement.builder()
                .accountNumber("LT12444444")
                .operationDate(LocalDateTime.now().plusDays(2))
                .beneficiary("John")
                .comment(null)
                .amount(111.99)
                .currency("USD")
                .build();
        BankStatement bankStatement3 = BankStatement.builder()
                .accountNumber("LT1211121")
                .operationDate(LocalDateTime.now().plusDays(3).plusHours(3))
                .beneficiary("Sarah")
                .comment(null)
                .amount(87d)
                .currency("USD")
                .build();
        entityManager.persistAndFlush(bankStatement);
        entityManager.persistAndFlush(bankStatement2);
        entityManager.persistAndFlush(bankStatement3);
        Specification<BankStatement> specification = Specification.where(isEqual("LT9111111"));
        specification = getRecordsByDate(LocalDate.now().minusDays(1), LocalDate.now().plusDays(3), specification);

        // Act
        List<BankStatement> actualBankStatements = repository.findAll(specification);

        // Assert
        Assertions.assertEquals(1, actualBankStatements.size());
        Assertions.assertEquals(bankStatement.getAccountNumber(), actualBankStatements.get(0).getAccountNumber());
        Assertions.assertEquals(bankStatement.getOperationDate(), actualBankStatements.get(0).getOperationDate());
    }

    @Test
    public void findAll_NoRecordsFound_ReturnsEmptyList() {
        // Arrange
        BankStatement bankStatement = BankStatement.builder()
                .accountNumber("LT9111111")
                .operationDate(LocalDateTime.now())
                .beneficiary("Colson")
                .comment(null)
                .amount(579.99)
                .currency("EUR")
                .build();
        entityManager.persistAndFlush(bankStatement);
        Specification<BankStatement> specification = Specification.where(isEqual("DOESNTEXIST"));
        specification = getRecordsByDate(LocalDate.now().minusDays(1), LocalDate.now().plusDays(3), specification);

        // Act
        List<BankStatement> actualBankStatements = repository.findAll(specification);

        // Assert
        Assertions.assertEquals(0, actualBankStatements.size());
    }
}

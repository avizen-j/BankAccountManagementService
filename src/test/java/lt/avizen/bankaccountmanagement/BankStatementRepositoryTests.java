package lt.avizen.bankaccountmanagement;

import lt.avizen.bankaccountmanagement.domain.BankStatement;
import lt.avizen.bankaccountmanagement.repository.BankStatementRepository;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import javax.validation.ConstraintViolationException;
import java.util.Date;
import java.util.Optional;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class BankStatementRepositoryTests {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private BankStatementRepository repository;

    @Test
    public void save_AllFieldsFilled_SavesRecord() {
        BankStatement bankStatement = BankStatement.builder()
                .accountNumber("LT9111111")
                .operationDate(new Date(System.currentTimeMillis()))
                .beneficiary("Colson")
                .comment("for a new guitar")
                .amount(579.99)
                .currency("EUR")
                .build();

        repository.saveAndFlush(bankStatement);

        Optional<BankStatement> actualBankStatement = repository.findById(bankStatement.getId());
        Assertions.assertThat((actualBankStatement.isPresent()));
        Assertions.assertThat(actualBankStatement.get().getAccountNumber().equals(bankStatement.getAccountNumber()));
        Assertions.assertThat(actualBankStatement.get().getOperationDate().equals(bankStatement.getOperationDate()));
        Assertions.assertThat(actualBankStatement.get().getBeneficiary().equals(bankStatement.getComment()));
        Assertions.assertThat(actualBankStatement.get().getComment().equals(bankStatement.getComment()));
        Assertions.assertThat(actualBankStatement.get().getAmount().equals(bankStatement.getAmount()));
        Assertions.assertThat(actualBankStatement.get().getCurrency().equals(bankStatement.getCurrency()));
    }

    @Test
    public void save_OptionalFieldIsNull_SavesRecord() {
        BankStatement bankStatement = BankStatement.builder()
                .accountNumber("LT9111111")
                .operationDate(new Date(System.currentTimeMillis()))
                .beneficiary("Colson")
                .comment(null)
                .amount(579.99)
                .currency("EUR")
                .build();

        repository.saveAndFlush(bankStatement);

        Optional<BankStatement> actualBankStatement = repository.findById(bankStatement.getId());
        Assertions.assertThat(actualBankStatement.isPresent());
        Assertions.assertThat(actualBankStatement.get().getAccountNumber().equals(bankStatement.getAccountNumber()));
        Assertions.assertThat(actualBankStatement.get().getOperationDate().equals(bankStatement.getOperationDate()));
        Assertions.assertThat(actualBankStatement.get().getBeneficiary().equals(bankStatement.getComment()));
        Assertions.assertThat(actualBankStatement.get().getComment() == null);
        Assertions.assertThat(actualBankStatement.get().getAmount().equals(bankStatement.getAmount()));
        Assertions.assertThat(actualBankStatement.get().getCurrency().equals(bankStatement.getCurrency()));
    }

    @Test(expected = ConstraintViolationException.class)
    public void save_RequiredFieldIsNull_ThrowsException() {
        BankStatement bankStatement = BankStatement.builder()
                .accountNumber(null)
                .operationDate(new Date(System.currentTimeMillis()))
                .beneficiary("Colson")
                .comment("for a new guitar")
                .amount(579.99)
                .currency("EUR")
                .build();

        repository.saveAndFlush(bankStatement);
    }

    @Test(expected = ConstraintViolationException.class)
    public void save_RequiredFieldIsEmpty_ThrowsException() {
        BankStatement bankStatement = BankStatement.builder()
                .accountNumber("")
                .operationDate(new Date(System.currentTimeMillis()))
                .beneficiary("Colson")
                .comment("for a new guitar")
                .amount(579.99)
                .currency("EUR")
                .build();

        repository.saveAndFlush(bankStatement);
    }
}

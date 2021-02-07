package lt.avizen.bankaccountmanagement.service;

import lt.avizen.bankaccountmanagement.domain.BankStatement;
import lt.avizen.bankaccountmanagement.model.ValidationResult;
import lt.avizen.bankaccountmanagement.repository.BankStatementRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;

import static lt.avizen.bankaccountmanagement.domain.BankStatementSpecification.getRecordsByDate;
import static lt.avizen.bankaccountmanagement.domain.BankStatementSpecification.isEqual;

@Service
public class ManagementServiceImpl implements ManagementService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ManagementServiceImpl.class);

    @Autowired
    CsvService csvService;

    @Autowired
    BankStatementRepository repository;

    @Autowired
    ValidationService validator;

    @Override
    public ValidationResult<BankStatement> uploadBankStatementCsv(byte[] csvByteArray) throws IOException {
        List<BankStatement> bankStatements = csvService.readCsvToItems(csvByteArray);

        ValidationResult<BankStatement> result = validator.validateList(bankStatements);
        if (result.getValidItemsCount() > 0) {
            repository.saveAll(result.getValidItems());
        }

        return result;
    }

    @Override
    public byte[] exportBankStatementCsv(LocalDate fromDate, LocalDate toDate) throws NoSuchElementException, IOException {
        List<BankStatement> bankStatements;
        Specification<BankStatement> specification = Specification.where(null);
        specification = getRecordsByDate(fromDate, toDate, specification);
        bankStatements = repository.findAll(specification);

        if (bankStatements.size() > 0) {
            return csvService.writeItemsToCsv(bankStatements);
        } else {
            throw new NoSuchElementException("No records were found");
        }
    }

    @Override
    public Double calculateAccountBalance(String accountNumber, LocalDate fromDate, LocalDate toDate) throws NoSuchElementException{
        Specification<BankStatement> specification = Specification.where(isEqual(accountNumber));
        specification = getRecordsByDate(fromDate, toDate, specification);
        List<BankStatement> bankStatements = repository.findAll(specification);

        if (bankStatements.size() > 0) {
            return bankStatements.stream().mapToDouble(BankStatement::getAmount).sum();
        } else {
            throw new NoSuchElementException(String.format("No records for '%s' account were found", accountNumber));
        }
    }
}

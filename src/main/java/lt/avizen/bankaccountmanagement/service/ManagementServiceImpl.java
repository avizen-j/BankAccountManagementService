package lt.avizen.bankaccountmanagement.service;

import lt.avizen.bankaccountmanagement.domain.BankStatement;
import lt.avizen.bankaccountmanagement.model.DataValidationResponse;
import lt.avizen.bankaccountmanagement.repository.BankStatementRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

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
    public void uploadBankStatementCsv(byte[] csvByteArray) throws IOException {
        List<BankStatement> bankStatements = csvService.readCsvToItems(csvByteArray);

        DataValidationResponse<BankStatement> response = validator.validateList(bankStatements);
        repository.saveAll(response.getValidItems());

        response.getValidationErrors().forEach(LOGGER::info);
        LOGGER.info(String.format("Errors count: %s", response.getInvalidItemsCount()));
        LOGGER.info("Inserted " + response.getValidItemsCount() + " records out of " + bankStatements.size());
    }

    @Override
    public byte[] exportBankStatementCsv(LocalDate fromDate, LocalDate toDate) throws IOException {
        List<BankStatement> bankStatements;
        Specification<BankStatement> specification = Specification.where(null);
        specification = getDateSpecification(fromDate, toDate, specification);
        bankStatements = repository.findAll(specification);

        return csvService.writeItemsToCsv(bankStatements);
    }

    @Override
    public Double calculateAccountBalance(String accountNumber, LocalDate fromDate, LocalDate toDate) {
        Specification<BankStatement> specification = Specification.where(isEqual(accountNumber));
        specification = getDateSpecification(fromDate, toDate, specification);
        List<BankStatement> bankStatements = repository.findAll(specification);

        return bankStatements.stream().mapToDouble(BankStatement::getAmount).sum();
    }

    private Specification<BankStatement> getDateSpecification(LocalDate fromDate, LocalDate toDate, Specification<BankStatement> specification) {
        if (fromDate != null) {
            specification = specification.and(isGreaterThanOrEqualTo(fromDate.atStartOfDay()));
        }

        if (toDate != null) {
            specification = specification.and(isLessThanOrEqualTo(toDate.atStartOfDay()));
        }
        return specification;
    }

    private Specification<BankStatement> isGreaterThanOrEqualTo(LocalDateTime date) {
        return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(root.get("operationDate"), date);
    }

    private Specification<BankStatement> isLessThanOrEqualTo(LocalDateTime date) {
        return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.lessThanOrEqualTo(root.get("operationDate"), date);
    }

    private Specification<BankStatement> isEqual(String accountNumber) {
        return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.equal(root.get("accountNumber"), accountNumber);
    }
}

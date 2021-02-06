package lt.avizen.bankaccountmanagement.service;

import lt.avizen.bankaccountmanagement.domain.BankStatement;
import lt.avizen.bankaccountmanagement.model.DataValidationResponse;
import lt.avizen.bankaccountmanagement.repository.BankStatementRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
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
    public byte[] exportBankStatementCsv() throws IOException {
        List<BankStatement> bankStatements = repository.findAll();
        byte[] csvByteArray = csvService.writeItemsToCsv(bankStatements);

        return csvByteArray;
    }
}

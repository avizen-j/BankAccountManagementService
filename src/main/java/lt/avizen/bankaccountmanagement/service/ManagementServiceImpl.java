package lt.avizen.bankaccountmanagement.service;

import lt.avizen.bankaccountmanagement.domain.BankStatement;
import lt.avizen.bankaccountmanagement.repository.BankStatementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class ManagementServiceImpl implements ManagementService{

    @Autowired
    CsvService csvService;

    @Autowired
    BankStatementRepository repository;

    @Override
    public List<BankStatement> uploadBankStatementCsv(byte[] csvByteArray) throws IOException {
        List<BankStatement> bankStatements = csvService.readCsvToItems(csvByteArray);
        repository.saveAll(bankStatements);

        return bankStatements;
    }

    @Override
    public byte[] exportBankStatementCsv() throws IOException{
        List<BankStatement> bankStatements = repository.findAll();
        byte[] csvByteArray = csvService.writeItemsToCsv(bankStatements);

        return csvByteArray;
    }
}

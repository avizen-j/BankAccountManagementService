package lt.avizen.bankaccountmanagement.service;

import lt.avizen.bankaccountmanagement.domain.BankStatement;
import lt.avizen.bankaccountmanagement.enums.BankStatementEnum;
import lt.avizen.bankaccountmanagement.utility.SafeStringParser;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class CsvServiceImpl implements CsvService {

    private final CSVFormat CSV_FORMAT = CSVFormat.DEFAULT.withDelimiter(',').withHeader(BankStatementEnum.class);

    @Override
    public byte[] writeItemsToCsv(List<BankStatement> items) throws IOException {

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        OutputStreamWriter writer = new OutputStreamWriter(output);
        CSVPrinter csvPrinter = new CSVPrinter(writer, CSV_FORMAT);

        try (output; writer; csvPrinter) {
            for (BankStatement item : items) {
                csvPrinter.printRecord(item.getAccountNumber(), item.getOperationDate(), item.getBeneficiary(),
                        item.getComment(), item.getAmount(), item.getCurrency());
            }
            csvPrinter.flush();

            return output.toByteArray();
        }
    }

    @Override
    public List<BankStatement> readCsvToItems(byte[] csvByteArr) throws IOException {

        List<BankStatement> bankStatements = new ArrayList<>();
        ByteArrayInputStream input = new ByteArrayInputStream(csvByteArr);
        InputStreamReader reader = new InputStreamReader(input);
        CSVParser csvParser = new CSVParser(reader, CSV_FORMAT.withSkipHeaderRecord(true));

        try (input; reader; csvParser) {
            for (CSVRecord record : csvParser) {
                String accountNumber = record.get(BankStatementEnum.accountNumber);
                Date operationDate = SafeStringParser.parseDate(record.get(BankStatementEnum.operationDate));
                String beneficiary = record.get(BankStatementEnum.beneficiary);
                String comment = record.get(BankStatementEnum.comment);
                Double amount = SafeStringParser.parseDouble(record.get(BankStatementEnum.amount));
                String currency = record.get(BankStatementEnum.currency);
                bankStatements.add(new BankStatement(accountNumber, operationDate, beneficiary, comment, amount, currency));
            }

            return bankStatements;
        }
    }
}

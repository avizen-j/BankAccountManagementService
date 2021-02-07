package lt.avizen.bankaccountmanagement.service;

import lt.avizen.bankaccountmanagement.domain.BankStatement;

import java.io.IOException;
import java.util.List;

public interface CsvService {

    byte[] writeItemsToCsv(List<BankStatement> items) throws IOException;

    List<BankStatement> readCsvToItems(byte[] csvByteArr) throws IOException;
}

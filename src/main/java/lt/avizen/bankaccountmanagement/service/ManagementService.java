package lt.avizen.bankaccountmanagement.service;

import java.io.IOException;
import java.time.LocalDate;

public interface ManagementService {

    void uploadBankStatementCsv(byte[] csvByteArray) throws IOException;

    byte[] exportBankStatementCsv(LocalDate fromDate, LocalDate toDate) throws IOException;

    Double calculateAccountBalance(String accountNumber, LocalDate fromDate, LocalDate toDate);
}

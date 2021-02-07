package lt.avizen.bankaccountmanagement.service;

import lt.avizen.bankaccountmanagement.domain.BankStatement;
import lt.avizen.bankaccountmanagement.model.ValidationResult;

import java.io.IOException;
import java.time.LocalDate;
import java.util.NoSuchElementException;

public interface ManagementService {

    ValidationResult<BankStatement> uploadBankStatementCsv(byte[] csvByteArray) throws IOException;

    byte[] exportBankStatementCsv(LocalDate fromDate, LocalDate toDate) throws NoSuchElementException, IOException;

    Double calculateAccountBalance(String accountNumber, LocalDate fromDate, LocalDate toDate) throws NoSuchElementException;
}

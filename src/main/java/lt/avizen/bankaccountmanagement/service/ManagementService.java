package lt.avizen.bankaccountmanagement.service;

import lt.avizen.bankaccountmanagement.domain.BankStatement;

import java.io.IOException;
import java.util.List;

public interface ManagementService {

    void uploadBankStatementCsv(byte[] csvByteArray) throws IOException;

    byte[] exportBankStatementCsv() throws IOException;
}

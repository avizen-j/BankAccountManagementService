package lt.avizen.bankaccountmanagement.service;

import lt.avizen.bankaccountmanagement.domain.BankStatement;
import lt.avizen.bankaccountmanagement.model.DataValidationResponse;

import java.util.List;

public interface ValidationService {
    <T> DataValidationResponse<T> validateList(List<T> listItems);
}

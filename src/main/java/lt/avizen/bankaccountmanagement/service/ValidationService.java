package lt.avizen.bankaccountmanagement.service;

import lt.avizen.bankaccountmanagement.model.ValidationResult;

import java.util.List;

public interface ValidationService {
    <T> ValidationResult<T> validateList(List<T> listItems);
}

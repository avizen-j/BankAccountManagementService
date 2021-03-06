package lt.avizen.bankaccountmanagement.service;

import lt.avizen.bankaccountmanagement.model.ValidationResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class ValidationServiceImpl implements ValidationService {

    @Autowired
    Validator validator;

    @Override
    public <T> ValidationResult<T> validateList(List<T> listItems) {

        int invalidItemsCount = 0;
        List<T> validItems = new ArrayList<>();
        List<String> validationErrors = new ArrayList<>();

        int iterator = 1;
        for (T item : listItems) {
            Set<ConstraintViolation<T>> constraintViolations = validator.validate(item);
            if (constraintViolations.size() == 0) {
                validItems.add(item);
            } else {
                invalidItemsCount++;
                StringBuilder errorMessage = new StringBuilder("Error occurred at position: ")
                        .append(iterator)
                        .append(". <property>: <error> - ");
                constraintViolations.forEach(s -> errorMessage.append(s.getPropertyPath())
                        .append(": ")
                        .append(s.getMessage())
                        .append("; "));
                validationErrors.add(errorMessage.toString());
            }
            iterator++;
        }

        return new ValidationResult<>(validItems.size(), invalidItemsCount, validItems, validationErrors);
    }
}

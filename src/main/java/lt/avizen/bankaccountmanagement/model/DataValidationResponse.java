package lt.avizen.bankaccountmanagement.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lt.avizen.bankaccountmanagement.domain.BankStatement;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DataValidationResponse<T> {
    private int validItemsCount;

    private int invalidItemsCount;

    private List<T> validItems;

    private List<String> validationErrors;
}


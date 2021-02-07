package lt.avizen.bankaccountmanagement.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ValidationResult<T> {

    private int validItemsCount;

    private int invalidItemsCount;

    private List<T> validItems;

    private List<String> validationErrors;
}


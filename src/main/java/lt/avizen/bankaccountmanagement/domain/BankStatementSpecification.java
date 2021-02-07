package lt.avizen.bankaccountmanagement.domain;

import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class BankStatementSpecification {

    public static Specification<BankStatement> getRecordsByDate(LocalDate fromDate, LocalDate toDate, Specification<BankStatement> specification) {
        if (fromDate != null) {
            specification = specification.and(isGreaterThanOrEqualTo(fromDate.atStartOfDay()));
        }

        if (toDate != null) {
            specification = specification.and(isLessThanOrEqualTo(toDate.atStartOfDay()));
        }
        return specification;
    }

    public static Specification<BankStatement> isGreaterThanOrEqualTo(LocalDateTime date) {
        return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(root.get("operationDate"), date);
    }

    public static Specification<BankStatement> isLessThanOrEqualTo(LocalDateTime date) {
        return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.lessThanOrEqualTo(root.get("operationDate"), date);
    }

    public static Specification<BankStatement> isEqual(String accountNumber) {
        return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.equal(root.get("accountNumber"), accountNumber);
    }
}

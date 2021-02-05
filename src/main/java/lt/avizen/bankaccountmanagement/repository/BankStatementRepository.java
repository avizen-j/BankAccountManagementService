package lt.avizen.bankaccountmanagement.repository;

import lt.avizen.bankaccountmanagement.domain.BankStatement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BankStatementRepository extends JpaRepository<BankStatement, Long> {
}

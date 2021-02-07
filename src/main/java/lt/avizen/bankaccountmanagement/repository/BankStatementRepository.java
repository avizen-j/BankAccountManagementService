package lt.avizen.bankaccountmanagement.repository;

import lt.avizen.bankaccountmanagement.domain.BankStatement;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BankStatementRepository extends JpaRepository<BankStatement, Long> {
    List<BankStatement> findAll(Specification<BankStatement> specification);
}

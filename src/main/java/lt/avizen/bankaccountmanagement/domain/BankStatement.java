package lt.avizen.bankaccountmanagement.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Table(name = "bankStatements")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BankStatement {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
    @NotEmpty
    private String accountNumber;

    @Column
    @NotNull
    private Date operationDate;

    @Column
    @NotEmpty
    private String beneficiary;

    @Column
    private String comment;

    @Column
    @NotNull
    private Double amount;

    @Column
    @NotEmpty
    private String currency;

    public BankStatement(String accountNumber, Date operationDate, String beneficiary, String comment, Double amount, String currency) {
        this.accountNumber = accountNumber;
        this.operationDate = operationDate;
        this.beneficiary = beneficiary;
        this.comment = comment;
        this.amount = amount;
        this.currency = currency;
    }
}

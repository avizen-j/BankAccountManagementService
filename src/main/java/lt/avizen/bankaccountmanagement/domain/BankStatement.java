package lt.avizen.bankaccountmanagement.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Table(name = "bankStatements")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BankStatement {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
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
}

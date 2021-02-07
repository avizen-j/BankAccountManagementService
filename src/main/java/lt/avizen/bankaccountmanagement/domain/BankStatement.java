package lt.avizen.bankaccountmanagement.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

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
    private LocalDateTime operationDate;

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

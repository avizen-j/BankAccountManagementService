package lt.avizen.bankaccountmanagement.controller;

import lt.avizen.bankaccountmanagement.domain.BankStatement;
import lt.avizen.bankaccountmanagement.model.AccountBalanceResponse;
import lt.avizen.bankaccountmanagement.model.ErrorResponse;
import lt.avizen.bankaccountmanagement.model.FileUploadResponse;
import lt.avizen.bankaccountmanagement.model.ValidationResult;
import lt.avizen.bankaccountmanagement.service.ManagementService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/v1")
public class ManagementController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ManagementController.class);
    private static final String CSV_TYPE = "text/csv";

    @Autowired
    ManagementService managementService;

    @GetMapping("/export")
    public ResponseEntity exportBankStatements(@RequestParam(required = false) String startDate,
                                               @RequestParam(required = false) String endDate) {

        try {
            LocalDate startLocalDate = startDate != null ? LocalDate.parse(startDate) : null;
            LocalDate endLocalDate = endDate != null ? LocalDate.parse(endDate) : null;
            byte[] csvByteArr = managementService.exportBankStatementCsv(startLocalDate, endLocalDate);
            return ResponseEntity.ok()
                    .header("Content-Disposition", "attachment; filename=report.csv")
                    .contentLength(csvByteArr.length)
                    .contentType(MediaType.parseMediaType("text/csv"))
                    .body(csvByteArr);
        } catch (DateTimeParseException | NoSuchElementException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
            return ResponseEntity.status(500).body(new ErrorResponse("Something went wrong.", ex.getMessage()));
        }
    }

    @GetMapping("/calculate")
    public ResponseEntity calculateAccountBalance(@RequestParam String accountNumber,
                                                  @RequestParam(required = false) String startDate,
                                                  @RequestParam(required = false) String endDate) {
        try {
            LocalDate startLocalDate = startDate != null ? LocalDate.parse(startDate) : null;
            LocalDate endLocalDate = endDate != null ? LocalDate.parse(endDate) : null;
            Double accountBalance = managementService.calculateAccountBalance(accountNumber, startLocalDate, endLocalDate);
            return ResponseEntity.ok()
                    .body(new AccountBalanceResponse(accountNumber, accountBalance));
        } catch (DateTimeParseException | NoSuchElementException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
            return ResponseEntity.status(500).body(new ErrorResponse("Something went wrong.", ex.getMessage()));
        }
    }

    @PostMapping("/upload")
    public FileUploadResponse uploadBankStatements(@RequestParam("file") MultipartFile file) {

        try {
            String incomingFileType = file.getContentType();
            if (CSV_TYPE.equals(incomingFileType)) {
                byte[] csvByteArray = file.getBytes();
                ValidationResult<BankStatement> result = managementService.uploadBankStatementCsv(csvByteArray);

                int initialBankStatementsCount = result.getValidItemsCount() + result.getInvalidItemsCount();
                return FileUploadResponse.builder()
                        .message("Inserted " + result.getValidItemsCount() + " records out of " + initialBankStatementsCount)
                        .errors(result.getValidationErrors())
                        .build();
            }
            return FileUploadResponse.builder()
                    .message(String.format("You have uploaded '%s'. Please upload valid CSV file.", incomingFileType))
                    .build();
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
            return FileUploadResponse.builder()
                    .message("Something went wrong. Please try again later.")
                    .build();
        }
    }

}

package lt.avizen.bankaccountmanagement.controller;

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

@RestController
@RequestMapping("/api/v1")
public class ManagementController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ManagementController.class);
    private static final String CSV_TYPE = "text/csv";

    @Autowired
    ManagementService managementService;

    // TODO: Replace with object type response.
    // TODO: Add standard exception handling.
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
        } catch (DateTimeParseException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage());
            return ResponseEntity.status(500).body("Something went wrong.");
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
                    .body(accountBalance);
        } catch (DateTimeParseException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage());
            return ResponseEntity.status(500).body("Something went wrong.");
        }
    }

    // TODO: Replace with object type response.
    // TODO: Add standard exception handling.
    @PostMapping("/upload")
    public ResponseEntity uploadBankStatements(@RequestParam("file") MultipartFile file) {

        try {
            String incomingFileType = file.getContentType();
            if (CSV_TYPE.equals(incomingFileType)) {
                byte[] csvByteArray = file.getBytes();
                managementService.uploadBankStatementCsv(csvByteArray);
                return ResponseEntity.ok().body("Success.");
            }
            return ResponseEntity.badRequest()
                    .body(String.format("You have uploaded '%s'. Please upload valid CSV file", incomingFileType));
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage());
            return ResponseEntity.status(500).body("Something went wrong.");
        }
    }

}

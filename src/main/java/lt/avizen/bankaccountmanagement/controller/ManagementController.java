package lt.avizen.bankaccountmanagement.controller;

import lombok.SneakyThrows;
import lt.avizen.bankaccountmanagement.domain.BankStatement;
import lt.avizen.bankaccountmanagement.service.ManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class ManagementController {

    private static final String CSV_TYPE = "text/csv";

    @Autowired
    ManagementService managementService;

    // TODO: Replace with object type response.
    // TODO: Add standard exception handling.
    @SneakyThrows
    @GetMapping("/export")
    public ResponseEntity<byte[]> exportBankStatements() {

        byte[] csvByteArr = managementService.exportBankStatementCsv();

        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=report.csv")
                .contentLength(csvByteArr.length)
                .contentType(MediaType.parseMediaType("text/csv"))
                .body(csvByteArr);
    }

    // TODO: Replace with object type response.
    // TODO: Add standard exception handling.
    // TODO: Add validation
    @PostMapping("/upload")
    public ResponseEntity uploadBankStatements(@RequestParam("file") MultipartFile file) {

        String incomingFileType = file.getContentType();
        if (CSV_TYPE.equals(incomingFileType)) {
            try {
                byte[] csvByteArray = file.getBytes();
                List<BankStatement> bankStatements = managementService.uploadBankStatementCsv(csvByteArray);

                return ResponseEntity.ok()
                        .body(bankStatements);
            } catch (Exception ex) {
                ex.printStackTrace();

                return ResponseEntity.status(500)
                        .body("Something went wrong.");
            }
        }

        return ResponseEntity.badRequest()
                .body(String.format("You have upload '%s'. Please upload valid CSV file", incomingFileType));
    }

}

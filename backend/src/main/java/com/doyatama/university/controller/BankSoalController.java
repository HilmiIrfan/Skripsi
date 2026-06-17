package com.doyatama.university.controller;

import com.doyatama.university.model.BankSoal;
import com.doyatama.university.payload.ApiResponse;
import com.doyatama.university.payload.BankSoalRequest;
import com.doyatama.university.payload.DefaultResponse;
import com.doyatama.university.payload.PagedResponse;
import com.doyatama.university.security.CurrentUser;
import com.doyatama.university.security.UserPrincipal;
import com.doyatama.university.service.BankSoalService;
import com.doyatama.university.util.AppConstants;
import java.io.IOException;
import java.net.URI;
import javax.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/api/bankSoal")
public class BankSoalController {

    private BankSoalService bankSoalService = new BankSoalService();

    @GetMapping
    public PagedResponse<BankSoal> getBankSoal(
            @RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
            @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size,
            @RequestParam(value = "userID", defaultValue = "*") String userID,
            @CurrentUser UserPrincipal currentUser) throws IOException {

        String schoolID = currentUser.getSchoolId();
        return bankSoalService.getAllBankSoal(page, size, userID, schoolID);
    }

    @PostMapping
    public ResponseEntity<?> createBankSoal(@Valid @RequestBody BankSoalRequest bankSoalRequest) throws IOException {
        try {
            BankSoal bankSoal = bankSoalService.createBankSoal(bankSoalRequest);

            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest().path("/{bankSoalId}")
                    .buildAndExpand(bankSoal.getIdBankSoal()).toUri();
            return ResponseEntity.created(location)
                    .body(new ApiResponse(true, "Soal Ujian Created Successfully"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse(false, e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, "An unexpected error occurred."));
        }
    }

    @GetMapping("/{bankSoalId}")
    public DefaultResponse<BankSoal> getBankSoalById(@PathVariable String bankSoalId) throws IOException {
        return bankSoalService.getBankSoalById(bankSoalId);
    }

    @PutMapping("/{bankSoalId}")
    public ResponseEntity<?> updateBankSoal(@PathVariable String bankSoalId,
            @Valid @RequestBody BankSoalRequest bankSoalRequest) throws IOException {
        try {
            BankSoal bankSoal = bankSoalService.updateBankSoal(bankSoalId, bankSoalRequest);

            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest().path("/{bankSoalId}")
                    .buildAndExpand(bankSoal.getIdBankSoal()).toUri();

            return ResponseEntity.created(location)
                    .body(new ApiResponse(true, "Bank Soal Updated Successfully"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse(false, e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, "An unexpected error occurred."));
        }
    }

    @DeleteMapping("/{bankSoalId}")
    public ResponseEntity<?> deleteBankSoal(@PathVariable String bankSoalId) throws IOException {
        try {
            bankSoalService.deleteBankSoalById(bankSoalId);

            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ApiResponse(true, "Soal Ujian Deleted Successfully"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse(false, e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, "An unexpected error occurred."));
        }
    }

}

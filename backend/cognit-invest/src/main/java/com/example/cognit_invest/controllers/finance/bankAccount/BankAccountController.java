package com.example.cognit_invest.controllers.finance.bankAccount;

import com.example.cognit_invest.DTOs.finance.bankAccount.BankAccountRequestDTO;
import com.example.cognit_invest.DTOs.finance.bankAccount.BankAccountResponseDTO;
import com.example.cognit_invest.components.SessionUser;
import com.example.cognit_invest.services.finance.BankAccountService;
import com.example.cognit_invest.services.internal.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bank-account")
public class BankAccountController {
    private final BankAccountService bankAccountService;

    public BankAccountController(BankAccountService bankAccountService) {
        this.bankAccountService = bankAccountService;
    }

    @GetMapping
    public ResponseEntity<List<BankAccountResponseDTO>> getAllAccounts(
            @AuthenticationPrincipal SessionUser sessionUser
            ){

        Logger.debug("Retornando contas bancarias");
        return ResponseEntity.ok(
                bankAccountService.getAllAccounts(sessionUser)
        );
    }

    @PostMapping
    public ResponseEntity<BankAccountResponseDTO> saveAccount(
            @RequestBody BankAccountRequestDTO dto,
            @AuthenticationPrincipal SessionUser sessionUser
            ){
        return ResponseEntity.ok(
                bankAccountService.saveAccount(dto,sessionUser)
        );
    }
}

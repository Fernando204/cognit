package com.example.cognit_invest.controllers.finance.movimentation;

import com.example.cognit_invest.DTOs.finance.movimentation.ConfirmMovimentationDTO;
import com.example.cognit_invest.DTOs.finance.movimentation.MovimentationRequestDTO;
import com.example.cognit_invest.DTOs.finance.movimentation.MovimentationResponseDTO;
import com.example.cognit_invest.components.SessionUser;
import com.example.cognit_invest.services.finance.MovimentationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/movimentations")
public class MovimentationController {
    private final MovimentationService movimentationService;

    public MovimentationController(MovimentationService movimentationService) {
        this.movimentationService = movimentationService;
    }

    @PostMapping
    public ResponseEntity<MovimentationResponseDTO> saveMovimentation(
            @RequestBody @Valid MovimentationRequestDTO dto,
            @AuthenticationPrincipal SessionUser sessionUser
    ) {
        MovimentationResponseDTO response = movimentationService.saveMovimentation(dto, sessionUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PatchMapping("/confirm")
    public ResponseEntity<MovimentationResponseDTO> confirmPending(
            @RequestBody @Valid ConfirmMovimentationDTO dto,
            @AuthenticationPrincipal SessionUser sessionUser
    ) {
        return ResponseEntity.ok(movimentationService.confirmPending(dto, sessionUser));
    }

    @GetMapping
    public ResponseEntity<List<MovimentationResponseDTO>> getMovimentations(
            @AuthenticationPrincipal SessionUser sessionUser
    ) {
        return ResponseEntity.ok(movimentationService.getMovimentations(sessionUser));
    }
}

package com.example.cognit_invest.controllers.finance.movimentation;


import com.example.cognit_invest.DTOs.finance.fixedMovimentation.FixedMovimentationRequestDTO;
import com.example.cognit_invest.DTOs.finance.fixedMovimentation.FixedMovimentationResponseDTO;
import com.example.cognit_invest.components.SessionUser;
import com.example.cognit_invest.services.finance.FixedMovimentationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/fixed-movimentations")
public class FixedMovimentationController {

    private final FixedMovimentationService fixedMovimentationService;

    public FixedMovimentationController(FixedMovimentationService fixedMovimentationService) {
        this.fixedMovimentationService = fixedMovimentationService;
    }

    @PostMapping
    public ResponseEntity<FixedMovimentationResponseDTO> create(
            @AuthenticationPrincipal SessionUser sessionUser,
            @Valid @RequestBody FixedMovimentationRequestDTO request
    ) {
        FixedMovimentationResponseDTO response =
                fixedMovimentationService.create(sessionUser.id(), request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<FixedMovimentationResponseDTO>> findAll(
            @AuthenticationPrincipal SessionUser sessionUser
    ) {
        return ResponseEntity.ok(fixedMovimentationService.findAll(sessionUser.id()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<FixedMovimentationResponseDTO> findById(
            @AuthenticationPrincipal SessionUser sessionUser,
            @PathVariable String id
    ) {
        return ResponseEntity.ok(fixedMovimentationService.findById(sessionUser.id(), id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<FixedMovimentationResponseDTO> update(
            @AuthenticationPrincipal SessionUser sessionUser,
            @PathVariable String id,
            @Valid @RequestBody FixedMovimentationRequestDTO request
    ) {
        return ResponseEntity.ok(fixedMovimentationService.update(sessionUser.id(), id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @AuthenticationPrincipal SessionUser sessionUser,
            @PathVariable String id
    ) {
        fixedMovimentationService.delete(sessionUser.id(), id);
        return ResponseEntity.noContent().build();
    }
}
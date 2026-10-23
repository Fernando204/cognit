package com.example.cognit_invest.components;

import com.example.cognit_invest.DTOs.internal.ErrorResponseDTO;
import com.example.cognit_invest.exceptions.user.*;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // ---------- Autenticação / conta ----------

    // 401 - mesma mensagem para "usuário não existe" e "senha errada",
    // evitando que alguém descubra quais e-mails estão cadastrados.
    @ExceptionHandler({UserNotFoundException.class, InvalidCredentialsException.class})
    public ResponseEntity<ErrorResponseDTO> handleInvalidLogin(RuntimeException ex, HttpServletRequest request) {
        com.example.cognit_invest.services.internal.Logger.debug("Erro em: "+request.getRequestURI());
        return build(HttpStatus.UNAUTHORIZED, "INVALID_CREDENTIALS",
                "E-mail ou senha inválidos", request);
    }

    // 403 - conta existe, mas ainda não confirmou o e-mail
    @ExceptionHandler(PendingConfirmationException.class)
    public ResponseEntity<ErrorResponseDTO> handlePendingConfirmation(PendingConfirmationException ex, HttpServletRequest request) {
        return build(HttpStatus.FORBIDDEN, "PENDING_CONFIRMATION",
                messageOr(ex, "Confirme seu e-mail para acessar a conta"), request);
    }

    // 403 - conta desativada
    @ExceptionHandler(DisabledAccountException.class)
    public ResponseEntity<ErrorResponseDTO> handleDisabledAccount(DisabledAccountException ex, HttpServletRequest request) {
        return build(HttpStatus.FORBIDDEN, "ACCOUNT_DISABLED",
                messageOr(ex, "Conta desativada"), request);
    }

    // 409 - cadastro com e-mail já existente
    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<ErrorResponseDTO> handleEmailAlreadyExists(EmailAlreadyExistsException ex, HttpServletRequest request) {
        return build(HttpStatus.CONFLICT, "EMAIL_ALREADY_EXISTS",
                messageOr(ex, "E-mail já cadastrado"), request);
    }

    // 410 - token de confirmação expirado
    @ExceptionHandler(ExpiredTokenException.class)
    public ResponseEntity<ErrorResponseDTO> handleExpiredToken(ExpiredTokenException ex, HttpServletRequest request) {
        return build(HttpStatus.GONE, "TOKEN_EXPIRED",
                messageOr(ex, "O link de confirmação expirou. Solicite um novo."), request);
    }

    // ---------- Erros de requisição ----------

    // 400 - validação de @Valid (caso você passe a usar nos DTOs)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDTO> handleValidation(MethodArgumentNotValidException ex, HttpServletRequest request) {
        Map<String, String> fields = new LinkedHashMap<>();
        ex.getBindingResult().getFieldErrors()
                .forEach(e -> fields.putIfAbsent(e.getField(), e.getDefaultMessage()));

        return build(HttpStatus.BAD_REQUEST, "VALIDATION_ERROR",
                "Dados inválidos", request, fields);
    }

    // 400 - JSON malformado ou body ausente
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponseDTO> handleUnreadable(HttpMessageNotReadableException ex, HttpServletRequest request) {
        return build(HttpStatus.BAD_REQUEST, "MALFORMED_REQUEST",
                "Corpo da requisição inválido ou ausente", request);
    }

    // 400 - ?token= ausente no /auth/confirm
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorResponseDTO> handleMissingParam(MissingServletRequestParameterException ex, HttpServletRequest request) {
        return build(HttpStatus.BAD_REQUEST, "MISSING_PARAMETER",
                "Parâmetro obrigatório ausente: " + ex.getParameterName(), request);
    }

    // 400 - tipo de parâmetro inválido
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponseDTO> handleTypeMismatch(MethodArgumentTypeMismatchException ex, HttpServletRequest request) {
        return build(HttpStatus.BAD_REQUEST, "INVALID_PARAMETER",
                "Valor inválido para o parâmetro: " + ex.getName(), request);
    }

    // 405
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponseDTO> handleMethodNotSupported(HttpRequestMethodNotSupportedException ex, HttpServletRequest request) {
        return build(HttpStatus.METHOD_NOT_ALLOWED, "METHOD_NOT_ALLOWED",
                "Método HTTP não suportado para esta rota", request);
    }

    // ---------- Fallback ----------

    // 500 - qualquer erro não mapeado (ex.: falha ao enviar e-mail, token não encontrado)
    // Nunca expõe ex.getMessage() ao cliente.
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDTO> handleGeneric(
            Exception ex,
            HttpServletRequest request) {

        log.error(
                "Erro não tratado na requisição: {} {}",
                request.getMethod(),
                request.getRequestURI(),
                ex
        );

        return build(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "INTERNAL_ERROR",
                "Erro interno. Tente novamente mais tarde.",
                request
        );
    }

    // ---------- Helpers ----------

    private String messageOr(Exception ex, String fallback) {
        String msg = ex.getMessage();
        return (msg == null || msg.isBlank()) ? fallback : msg;
    }

    private ResponseEntity<ErrorResponseDTO> build(HttpStatus status, String code, String message, HttpServletRequest request) {
        return build(status, code, message, request, null);
    }

    private ResponseEntity<ErrorResponseDTO> build(HttpStatus status, String code, String message,
                                                   HttpServletRequest request, Map<String, String> fields) {
        ErrorResponseDTO body = new ErrorResponseDTO(
                LocalDateTime.now(),
                status.value(),
                status.getReasonPhrase(),
                code,
                message,
                request.getRequestURI(),
                fields
        );
        return ResponseEntity.status(status).body(body);
    }
}
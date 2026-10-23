package com.example.cognit_invest.services.internal;

import com.example.cognit_invest.DTOs.auth.LoginRequestDTO;
import com.example.cognit_invest.DTOs.auth.LoginResponseDTO;
import com.example.cognit_invest.DTOs.auth.RegisterDTO;
import com.example.cognit_invest.enums.account.AccountStatus;
import com.example.cognit_invest.enums.auth.LoginStatus;
import com.example.cognit_invest.enums.auth.TokenType;
import com.example.cognit_invest.exceptions.user.*;
import com.example.cognit_invest.models.auth.LoginRegister;
import com.example.cognit_invest.models.auth.Token;
import com.example.cognit_invest.models.people.User;
import com.example.cognit_invest.repositories.auth.LoginRegisterRepository;
import com.example.cognit_invest.repositories.auth.TokenRepository;
import com.example.cognit_invest.repositories.auth.UserRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class AuthService {

    private final TokenRepository tokenRepository;
    private final EmailService emailService;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final LoginRegisterRepository loginRegisterRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(
            TokenRepository tokenRepository,
            EmailService emailService,
            JwtService jwtService,
            PasswordEncoder passwordEncoder,
            LoginRegisterRepository loginRegisterRepository, UserRepository userRepository) {
        this.loginRegisterRepository = loginRegisterRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.emailService = emailService;
        this.tokenRepository = tokenRepository;
    }

    @Transactional
    public void registerLoginAttempt(
            String email,
            LoginStatus status,
            HttpServletRequest request
    ) {
        String ipAddress = request.getRemoteAddr();
        String userAgent = request.getHeader("User-Agent");

        LoginRegister register = new LoginRegister(
                email,
                ipAddress,
                status,
                userAgent
        );

        loginRegisterRepository.save(register);
    }

    public LoginResponseDTO login(LoginRequestDTO dto,HttpServletRequest request,HttpServletResponse response){
        Logger.debug("Fazendo login");
        User user = userRepository.findByEmail(dto.email()).orElseThrow(()->{
            registerLoginAttempt(
                    dto.email(),
                    LoginStatus.USER_NOT_FOUND,
                    request
            );
            return new UserNotFoundException();
        });

        if(!passwordEncoder.matches(dto.password(), user.getPassword())){
            registerLoginAttempt(
                    dto.email(),
                    LoginStatus.INVALID_CREDENTIALS,
                    request
            );
            throw new InvalidCredentialsException();
        }

        if(user.getStatus() == AccountStatus.PENDING_CONFIRMATION) throw PendingConfirmationException.pendingEmailConfirmation();
        if(user.getStatus() == AccountStatus.INATIVE) throw new DisabledAccountException();
        appendAuthCookie(response,user);

        registerLoginAttempt(
                dto.email(),
                LoginStatus.SUCCESS,
                request
        );
        return LoginResponseDTO.fromEntity(user);
    }

    @Transactional
    public LoginResponseDTO register(RegisterDTO dto){
        if(userRepository.existsByEmail(dto.email())) throw new EmailAlreadyExistsException();

        User user = userRepository.save(
                new User(
                        dto.name(),
                        dto.email(),
                        dto.phone(),
                        dto.document(),
                        passwordEncoder.encode(dto.password()),
                        AccountStatus.PENDING_CONFIRMATION
                )
        );

        Token token = Token.createToken(user.getId(), TokenType.CONFIRMATION);
        tokenRepository.save(token);
        try {
            emailService.sendConfirmationEmail(user.getEmail(), token);
        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException("Erro ao enviar e-mail");
        }

        return LoginResponseDTO.fromEntity(user);
    }

    @Transactional
    public String confirmEmail(String token){
        Token verificationToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Token não encontrado"));

        if (verificationToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            tokenRepository.delete(verificationToken);
            throw new ExpiredTokenException();
        }

        User user = userRepository.findById(verificationToken.getUserId())
                .orElseThrow(UserNotFoundException::new);

        user.setStatus(AccountStatus.ACTIVE);
        userRepository.save(user);
        tokenRepository.delete(verificationToken);

        String html;
        try {
            html = new String(
                    getClass().getResourceAsStream("/static/email-confirmado.html").readAllBytes()
            );
        }catch (Exception ex){
            html = "E-mail confirmado, Você já pode fazer login!";
        }

        return html;
    }


    private void appendAuthCookie(HttpServletResponse response, User user) {
        String token = jwtService.generateToken(user);

        Cookie cookie = new Cookie("AUTH_TOKEN", token);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(60 * 60 * 24);
        cookie.setAttribute("SameSite", "none");

        response.addCookie(cookie);
    }

}

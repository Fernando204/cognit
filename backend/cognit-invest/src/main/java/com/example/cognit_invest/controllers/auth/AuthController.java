package com.example.cognit_invest.controllers.auth;

import com.example.cognit_invest.DTOs.auth.ChangePasswordDTO;
import com.example.cognit_invest.DTOs.auth.LoginRequestDTO;
import com.example.cognit_invest.DTOs.auth.LoginResponseDTO;
import com.example.cognit_invest.DTOs.auth.RegisterDTO;
import com.example.cognit_invest.components.SessionUser;
import com.example.cognit_invest.exceptions.user.UserNotFoundException;
import com.example.cognit_invest.models.people.User;
import com.example.cognit_invest.repositories.auth.UserRepository;
import com.example.cognit_invest.services.internal.AuthService;
import com.example.cognit_invest.services.internal.Logger;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final AuthService authService;

    public AuthController(AuthService authService,UserRepository userRepository) {
        this.userRepository = userRepository;
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(
            @RequestBody LoginRequestDTO dto,
            HttpServletResponse response,
            HttpServletRequest request
            ){
        return ResponseEntity.ok(
                authService.login(dto,request,response)
        );
    }

    @PostMapping("/register")
    public ResponseEntity<LoginResponseDTO> register(
            @RequestBody RegisterDTO dto
            ){
        com.example.cognit_invest.services.internal.Logger.debug("cadastrando usuário" );
        return ResponseEntity.ok(
                authService.register(dto)
        );
    }

    @GetMapping("/confirm")
    public ResponseEntity<String> confirmEmail(
        @RequestParam String token
    ){
        return ResponseEntity.ok()
                .header("Content-Type", "text/html; charset=UTF-8")
                .body(
                        authService.confirmEmail(token)
                );
    }

    @GetMapping("/session")
    public ResponseEntity<LoginResponseDTO>  sessionMethod(@AuthenticationPrincipal SessionUser sessionUser){
        if(sessionUser == null) throw new UserNotFoundException();
        User user = userRepository.findById(sessionUser.id()).orElseThrow(UserNotFoundException::new);
        Logger.info("Informações de usuário encontradas");
        return ResponseEntity.ok(LoginResponseDTO.fromEntity(user));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletResponse response) {
        Logger.debug("usuário fazendo logout");
        Cookie cookie = new Cookie("AUTH_TOKEN", "");
        cookie.setHttpOnly(true);
        cookie.setSecure(false);//o secure do cookie está false por enquanto por estar em um ambiente de teste
        cookie.setPath("/");
        cookie.setMaxAge(0);
        cookie.setAttribute("SameSite", "lax");
        response.addCookie(cookie);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/recover")
    public ResponseEntity<Void> recoverPasswordRequest(@RequestBody String email){
        Logger.debug("iniciando recuperação de senha para o e-mail: "+email);
        authService.recoverRequest(email);
        return ResponseEntity.ok().build();
    }
    @PostMapping("/recover/confirm")
    public ResponseEntity<Void> confirmRecoverToken(@RequestBody String code) {
        authService.confirmRecoverToken(code);
        return ResponseEntity.ok().build();
    }
    @PostMapping("/recover/change-password")
    public ResponseEntity<Void> changePassword(@RequestBody ChangePasswordDTO dto) {
        authService.changePassword(
                dto.code(),
                dto.email(),
                dto.password()
        );

        return ResponseEntity.ok().build();
    }
}

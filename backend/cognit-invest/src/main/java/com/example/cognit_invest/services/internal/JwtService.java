package com.example.cognit_invest.services.internal;

import com.example.cognit_invest.components.SessionUser;
import com.example.cognit_invest.models.people.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;



@Service
public class JwtService {

    private final String SECRET = "1B4<s8=m.OW2CHxg'*7Z61b4_gQ=Fk9L44l";
    private final String ADM_SECRET = "dslkjf9838ju9834:^`@*&*&¨DB*&E¨*&@H&*¨@!*N @&*!(SLKJ*897(*&";

    private Key getKey() {
        return Keys.hmacShaKeyFor(SECRET.getBytes());
    }
    private Key getAdmKey(){return Keys.hmacShaKeyFor(ADM_SECRET.getBytes());}

    public String generateToken(User user) {
        return Jwts.builder()
                .setSubject(user.getId())
                .claim("type","USER")
                .claim("name",user.getName())
                .setIssuer("HorseControll")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 86_400_000)) // 1 minuto
                .signWith(getKey(), SignatureAlgorithm.HS256)
                .compact();
    }



    public String extractUserIde(String token) {
        return getClaims(token,"USER").getSubject();
    }

    public boolean isTokenValid(String token,String tokenType) {
        try {
            Claims claims = getClaims(token,tokenType);
            return !claims.getExpiration().before(new Date());
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public String extractUserId(String token) {
        return getClaims(token,"USER").getSubject();
    }

    public String extractUserName(String token) {
        return getClaims(token,"USER").get("name", String.class);
    }

    public String extractUserRole(String token) {
        Object role = getClaims(token,"USER").get("role");
        return role != null ? role.toString() : null;
    }

    private Claims getClaims(String token,String tokenType) {
        Key key = tokenType.equals("ADM") ? getAdmKey() : getKey();
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public SessionUser getPrincipal(String token,String tokenType){
        Claims claims = getClaims(token,tokenType);

        String role = tokenType.equals("ADM") ? tokenType : (String) claims.get("role");

        return new SessionUser(
                claims.getSubject(),
                (String) claims.get("name"),
                role
        );
    }
}

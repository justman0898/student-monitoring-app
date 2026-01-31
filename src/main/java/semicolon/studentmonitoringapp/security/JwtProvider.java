package semicolon.studentmonitoringapp.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Date;


@Component
@AllArgsConstructor
public class JwtProvider{

    private final String SECRET_KEY;

    public String generateToken(UserPrincipal user) {
        return Jwts.builder()
                .subject(user.getUsername())
                .issuer("student-monitoring-app")
                .claim("roles", user.getAuthorities())
                .claim("id", user.getUser().getId())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 86400000))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    public boolean isValid(String token) {
        Claims claims = getClaims(token);

        return !claims.getExpiration().before(new Date());

    }

    private Claims getClaims(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .requireIssuer("student-monitoring-app")
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims;
    }

    public String getUsername(String token) {
        return getClaims(token).getSubject();

    }
}

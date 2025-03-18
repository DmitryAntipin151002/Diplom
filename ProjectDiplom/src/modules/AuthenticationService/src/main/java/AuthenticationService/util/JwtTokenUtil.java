package AuthenticationService.util;

import AuthenticationService.domain.exception.BadRefreshTokenException;
import AuthenticationService.domain.exception.TokenException;
import AuthenticationService.domain.exception.TokenExpiredException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

import static AuthenticationService.constants.SecurityConstants.*;

@Slf4j
@Component
public class JwtTokenUtil {

    @Value("${jwt.token.expired-access}")
    private long validityAccess;
    @Value("${jwt.token.expired-refresh}")
    private long validityRefresh;
    @Value("${jwt.token.expired-preAuth}")
    private long validityPreAuth;
    private String publicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAhkzU2adLEQjMbyIgcwDYRRiUPiW+Hn0pavQukDB2zh083Qlpfwk/J2jwbjEekbX6XRJlWkSkSt7VocJ9ishtSu8iPuzVBQ6lSQ/HqiQcUzY3dHpWgwFiYlML+PutyGomTRUFeYYY/4neTJbMEWZixhLveZ9jszhLwg9LD9kW67GtUTOqzErn8wXrT0YoOzBBvnW21pYoUwQ+lL2+z0Iu8dcBRVlEkUoUqwy06fzbjWuv/7vMrPCWnTyuNn2S5sx2Cm35YJzUnLbA0jLT88KhueyX7wv1pSfY9WAUk6ntpYh+qurTrdQhwiPKb4cQlncrolw+zoRqWJccNcX9/jxiZwIDAQAB";
    // Приватный ключ
    private String privateKey = "MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQCGTNTZp0sRCMxvIiBzANhFGJQ+Jb4efSlq9C6QMHbOHTzdCWl/CT8naPBuMR6RtfpdEmVaRKRK3tWhwn2KyG1K7yI+7NUFDqVJD8eqJBxTNjd0elaDAWJiUwv4+63IaiZNFQV5hhj/id5MlswRZmLGEu95n2OzOEvCD0sP2Rbrsa1RM6rMSufzBetPRig7MEG+dbbWlihTBD6Uvb7PQi7x1wFFWUSRShSrDLTp/NuNa6//u8ys8JadPK42fZLmzHYKbflgnNSctsDSMtPzwqG57JfvC/WlJ9j1YBSTqe2liH6q6tOt1CHCI8pvhxCWdyuiXD7OhGpYlxw1xf3+PGJnAgMBAAECggEALJuId2LhxB6wCJnFPW2iaBDp7FqM84OPJj9E2A/A5mU/ugtORyvDFkv80BzwkmVna1KY54q1J0ksnDYFjPtVkfYr4cq4Vp2uuL3NC+5CmkD/2mhoI8tp6k67Q1AVQORBJIk+8EZiNz6L39SWo5DmbfQMJtGSRA0G3MZHoFPzEQXKMHnleGDVETjjwp27yXH110na/Pu14mrxVarQ9+rWoTNMJLX5ahqdRhs8iWA3A3U6TvQtTRXNw7nhI3GBnrqr4LXtuIbMuLr94owVPnzxZmoQmkLKhfza1QpcYvg3SBQFDCmc3lc+onvN6TJEgiVK7vo4qaBEk4JAbVBiWoLygQKBgQD7IZ9E41wuyjzaqbxMNm3Yr/sN5cnJVJFkrWHenpwiFF5Bj8IwqZef0A3PoloFMNIC1doVuQmteN7QrBuhgyJi3gc+qXXBCQUAIF28VZgFPWD0gk/YyviX4OBv/RhuFXjef/78sji164QAhcSAY3Kd7JwMY2PTRQc8WMWxguyVPwKBgQCI516zaFdHk7ATDTNSYn4dThU2LEYsxo22/ePXgJMQBDYTQUdRPw2JQ1PqZnB/ku4wdFG3sMjFR0aR1c5cc/v9lrGjTpK9ZwIdrXs+0fsBri45t/YbDjhGy/5bJAPgdv8Buso9cJka0AJ6KnyvwmESUR2IFTfxoky/OLj82nYg2QKBgANbU7GsCFNnyKBznFy4bYdz8rsg0y2pv7fEyYYcQKID+66MqV/VJH2yXjzkQqfHG0eGbD/WFVcIQpIver8R7ae8qAQiZ7yIzlROGnKmAj4vr9gU1dRkAialjtpwW589EjyHIXVnPHLSDxWitBmzOpj5zKwtt+vfBFW+KgZRZbApAoGAQZY+q0qwjaz2bJolXTGsY5O128coM2lIGUPW9LY3PjQWR/E9HnJLpNfpiuFawfN9Qx9mys7Z0JFOAK3ieC5kqd+Y6LjK2gxQWjFzWuw0CWEN6j70m4+bbGdsSvOdG9tGXejD3N1lrA+9MBPMulMwOBs+P6SSsaNkizO8g/6RGzkCgYASycUIT1QOI3R6VFKyizcuKYyfIy9o4g82/eQ7xsxS7aWGIW7N7nF7kNgV0FMgMHupZFKnVtqctiCuzpFQRfcH7cebkaQ434CQb7SbgJooblKBRQpmvk4hF4K3U9IjIXMm+4zypEylPK/4IgREJd64j1oizYUW8yrexanY5wfPEQ==";

    public String createPreAuthorizedToken() {
        return Jwts.builder()
                .setExpiration(new Date(System.currentTimeMillis() + validityPreAuth))
                .signWith(RsaKeyUtils.getPrivateKey(privateKey), SignatureAlgorithm.RS256)
                .claim(TYPE, PRE_AUTHORIZE)
                .compact();
    }

    public String getTokenType(String token) {
        if (!token.isEmpty()) {
            String tokenWithoutBearer = token.substring(BEARER.length());
            Claims claims = Jwts.parserBuilder().setSigningKey(RsaKeyUtils.getPublicKey(publicKey)).build()
                    .parseClaimsJws(tokenWithoutBearer).getBody();
            return claims.get(TYPE, String.class);
        } else {
            throw new TokenException();
        }
    }

    public String createRecoveryToken(String userId) {
        return Jwts.builder()
                .setExpiration(new Date(System.currentTimeMillis() + validityPreAuth))
                .signWith(RsaKeyUtils.getPrivateKey(privateKey), SignatureAlgorithm.RS256)
                .claim(TYPE, RECOVERY)
                .claim(ID, userId)
                .compact();
    }

    public String createIsFirstEnterToken(String userId) {
        return Jwts.builder()
                .setExpiration(new Date(System.currentTimeMillis() + validityPreAuth))
                .signWith(RsaKeyUtils.getPrivateKey(privateKey), SignatureAlgorithm.RS256)
                .claim(TYPE, IS_FIRST_ENTER_TOKEN)
                .claim("id", userId)
                .compact();
    }

    public String createAccessToken(String userID) {
        return Jwts.builder()
                .setExpiration(new Date(System.currentTimeMillis() + validityAccess))
                .signWith(RsaKeyUtils.getPrivateKey(privateKey), SignatureAlgorithm.RS256)
                .claim(TYPE, ACCESS)
                .claim(ID, userID)
                .compact();
    }

    public String createRefreshToken(String userID) {
        return Jwts.builder()
                .setExpiration(new Date(System.currentTimeMillis() + validityRefresh))
                .signWith(RsaKeyUtils.getPrivateKey(privateKey), SignatureAlgorithm.RS256)
                .claim(TYPE, REFRESH)
                .claim(ID, userID)
                .compact();
    }

    public String getUserId(String token) {
        if (!token.isEmpty()) {
            String tokenWithoutBearer = token.substring(BEARER.length());
            Claims claims = Jwts.parserBuilder().setSigningKey(RsaKeyUtils.getPublicKey(publicKey)).build()
                    .parseClaimsJws(tokenWithoutBearer).getBody();
            return claims.get(ID, String.class);
        } else {
            throw new TokenException();
        }
    }

    public void checkRefreshToken(String refreshToken) {
        String tokenWithoutBearer = refreshToken.substring(BEARER.length());
        Claims claims;
        try {
            claims = Jwts.parserBuilder()
                    .setSigningKey(RsaKeyUtils.getPublicKey(publicKey))
                    .build()
                    .parseClaimsJws(tokenWithoutBearer).getBody();
        } catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException | SignatureException |
                 IllegalArgumentException exception) {
            throw new BadRefreshTokenException(REFRESH_TOKEN_PARSE_ERROR);
        }

        Date expirationTime = claims.getExpiration();

        if (expirationTime == null || expirationTime.before(new Date())) {
            throw new TokenExpiredException(REFRESH_TOKEN_HAS_EXPIRED_EXCEPTION);
        }
    }
}

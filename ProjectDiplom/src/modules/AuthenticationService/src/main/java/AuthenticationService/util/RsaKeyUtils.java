package AuthenticationService.util;

import AuthenticationService.domain.exception.RsaException;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import static AuthenticationService.constants.SecurityConstants.*;
@Slf4j
public class RsaKeyUtils {

    private RsaKeyUtils() {
    }


    public static PrivateKey getPrivateKey(String encodedPrivateKey) {
        log.debug("Received private key: {}", encodedPrivateKey);  // Добавьте логирование
        encodedPrivateKey = encodedPrivateKey.replaceAll("[^A-Za-z0-9+/=]", "");  // Убираем все невалидные символы
        try {
            byte[] decodedKey = Base64.getDecoder().decode(encodedPrivateKey);
            PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(decodedKey);
            KeyFactory factory = KeyFactory.getInstance(RSA);
            return factory.generatePrivate(spec);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            log.error(INVALID_PRIVATE_KEY + encodedPrivateKey);
            throw new RsaException(PRIVATE_KEY_EXCEPTION);
        }
    }
    public static PublicKey getPublicKey(String encodedPublicKey) {
        encodedPublicKey = encodedPublicKey.replace(" ", "");
        try {
            byte[] decodedKey = Base64.getDecoder().decode(encodedPublicKey); // Исправлено: убрали getBytes
            X509EncodedKeySpec spec = new X509EncodedKeySpec(decodedKey);
            KeyFactory factory = KeyFactory.getInstance(RSA);
            return factory.generatePublic(spec);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            log.error(INVALID_PUBLIC_KEY + encodedPublicKey);
            throw new RsaException(PUBLIC_KEY_EXCEPTION);
        }
    }
}

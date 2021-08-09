package br.com.jrafael.authutils.token.converter;

import br.com.jrafael.authutils.configuration.JwtConfig;
import com.nimbusds.jose.JWEObject;
import com.nimbusds.jose.crypto.DirectDecrypter;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jwt.SignedJWT;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.interfaces.RSAPublicKey;
import java.util.Date;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TokenConverter {

    private final static String TEMP_FOLDER = "/tmp/";
    private final static String WSO2_CERT_ALIAS = "wso2carbon";

    private final Environment env;
    private final JwtConfig jwtConfig;

    @SneakyThrows
    public String decryptToken(String encryptedToken) {
        log.info("Decrypting token");

        final JWEObject jweObject = JWEObject.parse(encryptedToken);

        final DirectDecrypter directDecrypter = new DirectDecrypter(this.jwtConfig.getPrivateKey().getBytes());

        jweObject.decrypt(directDecrypter);

        log.info("Token decrypted, returning signed token . . . ");

        return jweObject.getPayload().toSignedJWT().serialize();
    }

    @SneakyThrows
    public void validateTokenSignature(SignedJWT signedJWT) {
        log.info("Starting method to validate token signature...");

        InputStream certFile = null;
        final String currentProfile = this.env.getProperty("spring.profiles.active");
        if (currentProfile != null && currentProfile.contains("prod")) {
            log.info("Using prod enviromment");
            certFile = new FileInputStream(TEMP_FOLDER + WSO2_CERT_ALIAS + ".jks");
        } else {
            certFile = ClassLoader.getSystemResourceAsStream(WSO2_CERT_ALIAS + ".jks");
        }

        final KeyStore keystore = KeyStore.getInstance(KeyStore.getDefaultType());
        keystore.load(certFile, WSO2_CERT_ALIAS.toCharArray());

        final Certificate cert = keystore.getCertificate(WSO2_CERT_ALIAS);
        final RSAPublicKey publicKey = (RSAPublicKey) cert.getPublicKey();

        if (!signedJWT.verify(new RSASSAVerifier(publicKey))) {
            throw new AccessDeniedException("Invalid token signature!");
        }

        final Date now = new Date();
        final Date expirationTime = signedJWT.getJWTClaimsSet().getExpirationTime();
        if (expirationTime != null && now.after(expirationTime)) {
            throw new AccessDeniedException("Token expired!");
        }

        log.info("The token has a valid signature");
    }
}


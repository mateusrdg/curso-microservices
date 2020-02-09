package mateus.curso.microservicos.security.token.creator;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.DirectEncrypter;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import mateus.curso.microservicos.core.model.ApplicationUser;
import mateus.curso.microservicos.core.property.JwtConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPublicKey;
import java.util.Date;
import java.util.UUID;

import static java.util.stream.Collectors.toList;

@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TokenCreator {
    private final JwtConfiguration jwtConfiguration;

    @SneakyThrows
    public SignedJWT createSignedJWT(Authentication auth) {
        log.info("Criando um JWT assinado");
        ApplicationUser applicationUser = (ApplicationUser) auth.getPrincipal();
        JWTClaimsSet jwtClaimsSet = createJwtClaimsSet(auth, applicationUser);
        KeyPair rsaKeys = generateKeyPair();
        log.info("Building JWK from the RSA Keys");

        JWK jwk = new RSAKey.Builder((RSAPublicKey) rsaKeys.getPublic()).keyID(UUID.randomUUID().toString()).build();
        SignedJWT signedJWT = new SignedJWT(new JWSHeader.Builder(JWSAlgorithm.RS256)
                .jwk(jwk)
                .type(JOSEObjectType.JWT)
                .build(), jwtClaimsSet);
        log.info("Assinando token com a chave privada RSA");
        RSASSASigner signer = new RSASSASigner(rsaKeys.getPrivate());
        signedJWT.sign(signer);
        log.info("token serializado '{}'", signedJWT.serialize());
        return signedJWT;
    }

    private JWTClaimsSet createJwtClaimsSet(Authentication auth, ApplicationUser applicationUser) {
        log.info("Criando o objeto JWTClaimsSet para" +
                " '{}'", applicationUser);

        return new JWTClaimsSet.Builder()
                .subject(applicationUser.getUsername())
                .claim("authorities", auth.getAuthorities()
                        .stream()
                        .map(GrantedAuthority::getAuthority).collect(toList()))
                .issuer("http://mateus.curso")
                .issueTime(new Date())
                .expirationTime(new Date(System.currentTimeMillis() + (jwtConfiguration.getExpiration() * 1000)))
                .build();
    }

    @SneakyThrows
    private KeyPair generateKeyPair() {
        log.info("Gerando chaves RSA 2048 bits");
        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(2048);
        return generator.genKeyPair();
    }

    public String encryptToken(SignedJWT signedJWT) throws JOSEException {
        log.info("iniciando encriptacao do token");
        DirectEncrypter directEncrypter = new DirectEncrypter(jwtConfiguration.getPrivateKey().getBytes());
        JWEObject jweObject = new JWEObject(new JWEHeader.Builder(JWEAlgorithm.DIR, EncryptionMethod.A128CBC_HS256)
                .contentType("JWT")
                .build(), new Payload(signedJWT));
        log.info("encriptando token com a chave privada do sistema");
        jweObject.encrypt(directEncrypter);
        log.info("token encriptado");
        return jweObject.serialize();
    }
}

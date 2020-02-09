package mateus.curso.microservicos.auth.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jwt.SignedJWT;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import mateus.curso.microservicos.core.model.ApplicationUser;
import mateus.curso.microservicos.core.property.JwtConfiguration;
import mateus.curso.microservicos.security.token.creator.TokenCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class JwtUsernameAndPasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;
    private final JwtConfiguration jwtConfiguration;
    private final TokenCreator tokenCreator;

    @Override
    @SneakyThrows
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {
        log.info("Tentando autenticar. . .");
        ApplicationUser applicationUser = new ObjectMapper().readValue(request.getInputStream(), ApplicationUser.class);
        if (applicationUser == null)
            throw new UsernameNotFoundException("username ou password nao encontrados");
        log.info("Criando o objeto de autenticacao para o usuario '{}' e chamando UserDetailsServiceImpl", applicationUser.getUsername());
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(applicationUser.getUsername(), applicationUser.getPassword(), Collections.emptyList());
        usernamePasswordAuthenticationToken.setDetails(applicationUser);

        return authenticationManager.authenticate(usernamePasswordAuthenticationToken);
    }

    @Override
    @SneakyThrows
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication auth) throws IOException, ServletException {
        log.info("Autenticacao concluida com sucesso para o usuario: '{}', gerando token JWT", auth.getName());
        SignedJWT signedJWT = tokenCreator.createSignedJWT(auth);
        String encryptedToken = tokenCreator.encryptToken(signedJWT);
        log.info("token gerado com sucesso, adicionando ao response header");
        response.addHeader("Access-Control-Expose-Headers", "XRSF-TOKEN, " + jwtConfiguration.getHeader().getName());
        response.addHeader(jwtConfiguration.getHeader().getName(), jwtConfiguration.getHeader().getPrefix() + encryptedToken);
    }

}

package mateus.curso.microservicos.auth.security.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mateus.curso.microservicos.core.model.ApplicationUser;
import mateus.curso.microservicos.core.repository.ApplicationUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.Collection;

@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Service
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {

    private final ApplicationUserRepository applicationUserRepository;

    @Override
    public UserDetails loadUserByUsername(String username) {
        log.info("Procurando na DB o usuario pelo nome de usuario '{}' ", username);
        ApplicationUser applicationUser = applicationUserRepository.findByUsername(username);
        log.info("Usuario encontrado: '{}'", applicationUser);
        if (applicationUser == null)
            throw new UsernameNotFoundException("Usuario nao encontrado: " + username);
        return new CustomUserDetails(applicationUser);
    }

    public static final class CustomUserDetails extends ApplicationUser implements UserDetails {
        CustomUserDetails(@NotNull ApplicationUser applicationUser) {
            super(applicationUser);
        }

        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
            return AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_" + this.getRole());
        }

        @Override
        public String getPassword() {
            return this.getPassword();
        }

        @Override
        public String getUsername() {
            return this.getUsername();
        }

        @Override
        public boolean isAccountNonExpired() {
            return true;
        }

        @Override
        public boolean isAccountNonLocked() {
            return true;
        }

        @Override
        public boolean isCredentialsNonExpired() {
            return true;
        }

        @Override
        public boolean isEnabled() {
            return true;
        }
    }
}

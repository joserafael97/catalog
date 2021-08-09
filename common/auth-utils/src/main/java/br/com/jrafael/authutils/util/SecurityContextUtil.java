package br.com.jrafael.authutils.util;

import br.com.jrafael.authutils.model.UserAccount;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Slf4j
public class SecurityContextUtil {
    private SecurityContextUtil() {

    }

    public static void setSecurityContext(SignedJWT signedJWT) {
        try {
            final JWTClaimsSet claims = signedJWT.getJWTClaimsSet();
            final String username = claims.getSubject();
            if (username == null) {
                throw new JOSEException("Username missing from JWT");
            }

            final List<String> authorities = claims.getStringListClaim("authorities");
            final UserAccount applicationUser = UserAccount
                    .builder()
                    .id(claims.getLongClaim("userId"))
                    .username(username)
                    .role(String.join(",", authorities))
                    .build();
            final UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(applicationUser, null,
                    createAuthorities(authorities));
            auth.setDetails(signedJWT.serialize());

            SecurityContextHolder.getContext().setAuthentication(auth);
        } catch (final Exception e) {
            log.error("Error setting security context ", e);
            SecurityContextHolder.clearContext();
        }
    }

    private static List<SimpleGrantedAuthority> createAuthorities(List<String> authorities) {
        return authorities.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(toList());
    }
}

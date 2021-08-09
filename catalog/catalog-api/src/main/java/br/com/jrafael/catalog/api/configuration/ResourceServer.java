package br.com.jrafael.catalog.api.configuration;

import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class ResourceServer extends WebSecurityConfigurerAdapter {

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.cors().disable()
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/**").permitAll()
                //======================= SWAGGER ==============================//
                .antMatchers("/v2/api-docs").permitAll()
                .antMatchers("/actuator/**").permitAll()
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

    }

    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers(new String[]{"/v2/api-docs", "/swagger-resources", "/swagger-resources/**", "/configuration/ui", "/configuration/security", "/swagger-ui.html", "/webjars/**", "/v3/api-docs/**", "/swagger-ui/**"});
    }

}

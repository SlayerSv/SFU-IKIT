package PR8;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
            .authorizeHttpRequests(auth -> auth
            .requestMatchers("/styles.css").permitAll()
            .requestMatchers("/users/signup").permitAll()
            .requestMatchers("/users/login").permitAll()
            .requestMatchers("/products/").permitAll()
            .requestMatchers("/").permitAll()
            .requestMatchers("/products/add").hasAuthority("admin")
            .requestMatchers("/products/edit").hasAuthority("admin")
            .requestMatchers("/products/delete").hasAuthority("admin")
            .anyRequest().authenticated()
            )
            .formLogin(form -> form
            .loginPage("/users/login").permitAll()
            .loginProcessingUrl("/users/login")
            )
            .logout(logout -> logout.logoutUrl("/users/logout").permitAll())
            .exceptionHandling(except -> except
            .accessDeniedPage("/products/forbidden")
            )
            .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

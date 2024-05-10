package PR6;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import jakarta.servlet.DispatcherType;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
            //.csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(auth -> auth
            //.dispatcherTypeMatchers(DispatcherType.FORWARD).permitAll()
            .anyRequest().permitAll()
            // .requestMatchers("/products/login").permitAll()
            // .requestMatchers("/products/all").hasAnyRole("admin", "user")
            // .requestMatchers("/products/filter").hasAnyRole("admin", "user")
            // .requestMatchers("/products/filterPrice").hasAnyRole("admin", "user")
            // .requestMatchers("/products/add").hasRole("admin")
            // .requestMatchers("/products/edit").hasRole("admin")
            // .requestMatchers("/products/delete").hasRole("admin")
            //)
            //.formLogin((form) -> form
            //.loginPage("/login")
            //.permitAll()
        )
        //.logout((logout) -> logout.permitAll())
            .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

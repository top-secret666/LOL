package by.vstu.zamok.lab4.config;

import by.vstu.zamok.lab4.security.CustomUserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity
@EnableWebSecurity
public class WebSecurityConfig {

    private final CustomUserDetailService userDetailService;

    public WebSecurityConfig(CustomUserDetailService userDetailService) {
        this.userDetailService = userDetailService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth ->
                        auth
                                // GET запросы доступны всем (включая неаутентифицированных - guest)
                                .requestMatchers("GET", "/api/group/**", "/api/student/**", "/api/leader/**").permitAll()

                                // POST, PUT, DELETE доступны только ADMIN
                                .requestMatchers("POST", "/api/group/**", "/api/student/**", "/api/leader/**").hasRole("ADMIN")
                                .requestMatchers("PUT", "/api/group/**", "/api/student/**", "/api/leader/**").hasRole("ADMIN")
                                .requestMatchers("DELETE", "/api/group/**", "/api/student/**", "/api/leader/**").hasRole("ADMIN")

                                // MANAGER может делать только PUT запросы
                                .requestMatchers("PUT", "/api/group/**", "/api/student/**", "/api/leader/**").hasRole("MANAGER")

                                .anyRequest().authenticated()
                )
                .httpBasic(httpBasic -> {});

        http.authenticationProvider(authenticationProvider());

        return http.build();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider auth = new DaoAuthenticationProvider();
        auth.setUserDetailsService(userDetailService);
        auth.setPasswordEncoder(passwordEncoder());
        return auth;
    }
}

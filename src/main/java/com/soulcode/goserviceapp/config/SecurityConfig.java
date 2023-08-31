package com.soulcode.goserviceapp.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;


@Configuration
@EnableWebSecurity
public class SecurityConfig {

<<<<<<< HEAD
    private final String[] PUBLIC_ROUTES = {"/", "/home", "/auth/**", "/css/**", "/js/**", "/assets/**","/api/**"};
=======
    private final String[] PUBLIC_ROUTES = {"/", "/home", "/auth/**", "/css/**", "/js/**", "/assets/**", "/api/**"};
>>>>>>> 4fd336c9acba17650f0de7db8936f954fa0495f5

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .requestMatchers(PUBLIC_ROUTES)
                .permitAll()
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .requestMatchers("/prestador/**").hasRole("PRESTADOR")
                .requestMatchers("/cliente/**").hasRole("CLIENTE")
                .anyRequest()
                .authenticated()
                .and()
                .formLogin()
                .loginPage("/auth/login")
                .defaultSuccessUrl("/");
        return http.build();
    }
}

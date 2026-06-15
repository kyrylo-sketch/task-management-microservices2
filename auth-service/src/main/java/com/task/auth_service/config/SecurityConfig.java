package com.task.auth_service.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration//oznacza ze tu sa beany
@EnableWebSecurity//wlacza Spring Security i wylacza domyslna konfiguracje
public class SecurityConfig {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtFilter jwtFilter;

    @Bean//securityfilterchain - serce konfiguracji
    //lancuch filtrow prze ktory przechodzi kazke zadani http
    public SecurityFilterChain securityFilterChain(HttpSecurity http){

        return http
                //wylaczenie csrf zeby jwt mog dzilac
                .csrf(customizer -> customizer.disable())
                //cors mowie kto moze rozmawiac z naszym backendem
                .cors(cors -> cors.configurationSource(request -> {
                    var config = new org.springframework.web.cors.CorsConfiguration();
                    //mowi z jakiego adresu ktos moze uzyc
                    config.setAllowedOrigins(java.util.List.of(
                            "http://localhost:3000",
                            "https://task-management-frontend-nu-self.vercel.app"
                    ));
                    //mowi jakie medoty
                    config.setAllowedMethods(java.util.List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
                    //mowi jakie naglowki
                    config.setAllowedHeaders(java.util.List.of("*"));
                    //potrzebne gdy wysylamy ciasteczka lub autoryzacje
                    config.setAllowCredentials(true);
                    return config;
                }))
                //reguly kto moze co
                .authorizeHttpRequests(request -> request
                        .requestMatchers("/api/auth/register","/api/auth/login", "/api/auth/refresh",
                                "/swagger-ui/**", "/v3/api-docs/**", "/ws/**")
                        //te wszystkie bez tokenu
                        .permitAll()
                        //wszystkie pozostale po logowaniu
                        .anyRequest().authenticated())
                //wlacza logowanei przez naglowek Authorization: Basic base64(user:pass)
                .httpBasic(Customizer.withDefaults())
                //mowi o tym zeby nie tworzyc sesji http i nie pamietac uzetkownika miedzy zadaniami
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                //wstawie filter jwt przed standartowym filtrem spring
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .build();

    }

    @Bean
    //AuthenticationProvider - wie jak sprawdzic haslo uzytkownika
    public AuthenticationProvider authenticationProvider(){
        //pobiera uzytkownika z bazy przez UserDetailsService
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userDetailsService);
        //haszuje haslo algorytmem BCrypt z sila 12
        provider.setPasswordEncoder(new BCryptPasswordEncoder(12));
        // provider.setUserDetailsPasswordService(userDetailsService);
        return provider;
    }

    @Bean
    //przyjmuje dane logawania i zwraca Authentication
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config){
        return config.getAuthenticationManager();
    }

}

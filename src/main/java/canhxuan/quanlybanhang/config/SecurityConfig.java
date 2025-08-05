package canhxuan.quanlybanhang.config;

import canhxuan.quanlybanhang.entity.Token;
import canhxuan.quanlybanhang.entity.User;
import canhxuan.quanlybanhang.repository.TokenRepository;
import canhxuan.quanlybanhang.repository.UserRepository;
import canhxuan.quanlybanhang.security.CustomOAuth2UserService;
import canhxuan.quanlybanhang.security.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private CustomOAuth2UserService customOAuth2UserService;
    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private TokenRepository tokenRepository;
    @Autowired
    private UserRepository userRepository;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/quanlybanhang/auth/**", "/oauth2/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/quanlybanhang/products/**").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/quanlybanhang/products/**").hasRole("ADMIN")
                        .anyRequest().authenticated()
                        )
                .oauth2Login(oauth2 -> oauth2
                        .userInfoEndpoint(userInfor -> userInfor
                                .userService(customOAuth2UserService)
                        )
                        .successHandler((request, response, authentication) -> {
                            OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();
                            String username = oauth2User.getAttribute("name");
                            User user = userRepository.findByUsername(username).orElseThrow();
                            String jwtToken = jwtUtils.generateJwtToken(authentication);
                            String refreshToken = jwtUtils.generateRefreshToken(authentication);
                            tokenRepository.save(new Token(jwtToken, false, false, user));
                            tokenRepository.save(new Token(refreshToken, false, false, user));
                            response.setContentType("application/json");
                            System.out.println("JwtToken: " + jwtToken);
                            System.out.println("refreshToken: " + refreshToken);
                            response.getWriter().write("{\"token\": \"" + jwtToken + "\"," +
                                    "\n{\"RefreshToken\": \"" + refreshToken + "\",");
                        })
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}

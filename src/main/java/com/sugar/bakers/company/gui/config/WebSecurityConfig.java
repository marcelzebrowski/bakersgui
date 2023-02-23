package com.sugar.bakers.company.gui.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig{

    private final boolean IS_DEBUG_ENABLED = false;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf()
                .disable()
                    .formLogin(form->form.loginPage("/login")
                        .usernameParameter("username")
                        .passwordParameter("password")
                        .successHandler(myAuthenticationSuccessHandler()).permitAll()).logout(LogoutConfigurer::permitAll);
        http.authorizeHttpRequests((authentication) ->
                authentication.anyRequest().authenticated()
        );

        return http.build();
    }

    @Bean
    public AuthenticationSuccessHandler myAuthenticationSuccessHandler(){
        return new DefaultAuthentificationSuccessHandler();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.debug(IS_DEBUG_ENABLED)
                .ignoring().antMatchers("/resources/**", "/static/**", "/css/**", "/js/**", "/img/**", "/icon/**");
    }

    @Bean
    public UserDetailsService userDetailsService(BCryptPasswordEncoder bCryptPasswordEncoder) {
        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
        createMockUser(bCryptPasswordEncoder, manager, "Marcel");
        createMockUser(bCryptPasswordEncoder, manager, "Dennis");
        createMockUser(bCryptPasswordEncoder, manager, "Max");
        createMockUser(bCryptPasswordEncoder, manager, "Robert");
        return manager;
    }

    private void createMockUser(BCryptPasswordEncoder bCryptPasswordEncoder, InMemoryUserDetailsManager manager, String name){

        manager.createUser(User.withUsername(name)
                .password(bCryptPasswordEncoder.encode(name))
                .roles("USER")
                .build());
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder()
    {
        return new BCryptPasswordEncoder();
    }
}

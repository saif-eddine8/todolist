package com.todolist_alx.todolist.config

import com.todolist_alx.todolist.service.CUserService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.builders.WebSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain

@EnableWebSecurity
@Configuration
class SecurityConfig (val cUserService: CUserService) {

    @Bean
    fun webSecurityCustomizer(): WebSecurityCustomizer =
        WebSecurityCustomizer { web: WebSecurity ->
            web.ignoring().requestMatchers("/h2/**", "$API_PREFIX/register")
        }

    @Bean
    fun passwordEncoder(): PasswordEncoder = BCryptPasswordEncoder()

    @Bean
    fun authenticationProvider(): AuthenticationProvider {
        val daoAuthenticationProvider = DaoAuthenticationProvider()
        daoAuthenticationProvider.setUserDetailsService(cUserService)
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder())
        return daoAuthenticationProvider
    }

    @Bean
    @Throws(Exception::class)
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain =
        http.csrf { csrf ->
                csrf.disable() } // Disable CSRF for simplicity
            .authorizeHttpRequests { auth ->
                auth.anyRequest().authenticated() }
            .httpBasic {}
            .formLogin(Customizer.withDefaults())
            .build()

}
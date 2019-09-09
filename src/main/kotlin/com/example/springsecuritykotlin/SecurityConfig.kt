package com.example.springsecuritykotlin

import org.springframework.context.annotation.Bean
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.core.userdetails.User
import org.springframework.security.provisioning.InMemoryUserDetailsManager

@EnableWebSecurity
class SecurityConfig : WebSecurityConfigurerAdapter() {
    @Bean override fun userDetailsService() = InMemoryUserDetailsManager().also {
        val user = User
            .withDefaultPasswordEncoder()
            .username("nphat")
            .password("nphat")
            .roles("USER")
            .build()
        it.createUser(user)
    }

    override fun configure(http: HttpSecurity) {
        http.authorizeRequests()
            .antMatchers("/", "/home").permitAll()
            .anyRequest().authenticated()
        http.formLogin()
            .defaultSuccessUrl("/profile")
            .permitAll()
        http.logout().permitAll()
    }
}
package com.example.researchanything.config

import com.example.researchanything.security.jwt.JwtAuthEntryPoint
import com.example.researchanything.security.jwt.JwtAuthFilter
import com.example.researchanything.service.JwtUserDetailService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
class WebSecurity(
    private val entryPoint: JwtAuthEntryPoint,
    private val userDetailService: JwtUserDetailService,
    private val requestFilter: JwtAuthFilter
) : WebSecurityConfigurerAdapter() {

    @Bean override fun authenticationManagerBean(): AuthenticationManager {
        return super.authenticationManagerBean()
    }

    @Bean fun pwEncoder() = BCryptPasswordEncoder()

    @Autowired
    fun configureGlobal(auth: AuthenticationManagerBuilder) {
        auth.userDetailsService(userDetailService).passwordEncoder(pwEncoder())
    }

    override fun configure(http: HttpSecurity) {
        http.csrf().disable()
        http.authorizeRequests().antMatchers("/auth").permitAll().antMatchers("/h2-console/**/*").permitAll()
            .anyRequest().authenticated()
        http.exceptionHandling().authenticationEntryPoint(entryPoint)
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        http.addFilterBefore(requestFilter, UsernamePasswordAuthenticationFilter::class.java)
    }
}
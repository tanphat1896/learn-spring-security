package com.example.researchanything.controller

import com.example.researchanything.data.LoginRequest.LoginCredential
import com.example.researchanything.security.jwt.JwtTokenProvider
import com.example.researchanything.service.JwtUserDetailService
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import javax.servlet.http.HttpServletResponse

@CrossOrigin
@RestController
class TestController(
    private val authenticationManager: AuthenticationManager,
    private val jwtTokenProvider: JwtTokenProvider,
    private val jwtUserDetailService: JwtUserDetailService
) {
    @PostMapping("/auth")
    fun login(@RequestBody body: LoginCredential, res: HttpServletResponse) {
        authenticationManager.authenticate(UsernamePasswordAuthenticationToken(body.username, body.password))
        val userDetail = jwtUserDetailService.loadUserByUsername(body.username)
        val token = jwtTokenProvider.generateToken(userDetail)
        res.setHeader("Token", token)
    }

    @GetMapping("/profile")
    fun profile(): String {
        return "profile"
    }
}
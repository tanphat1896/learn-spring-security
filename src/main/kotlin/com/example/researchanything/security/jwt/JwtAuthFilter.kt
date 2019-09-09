package com.example.researchanything.security.jwt

import com.example.researchanything.service.JwtUserDetailService
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class JwtAuthFilter(
    private val userDetailService: JwtUserDetailService,
    val tokenProvider: JwtTokenProvider
) : OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val tokenHeader = request.getHeader("Authorization") ?: return filterChain.doFilter(request, response)
        if (isInvalidToken(tokenHeader)) {
            println("Invalid token, not started with Bearer")
            return filterChain.doFilter(request, response)
        }
        val jwtToken = tokenHeader.drop(7)
        val username = try {
            tokenProvider.getUsernameFromToken(jwtToken)
        } catch (e: Exception) {
            println("========> Fail to get username. ${e.message}")
            e.printStackTrace()
            return filterChain.doFilter(request, response)
        }
        if (SecurityContextHolder.getContext().authentication != null) {
            return filterChain.doFilter(request, response)
        }

        val userDetail = userDetailService.loadUserByUsername(username)
        if (!tokenProvider.validateToken(jwtToken, userDetail)) {
            return filterChain.doFilter(request, response)
        }

        val usernamePwAuthToken = UsernamePasswordAuthenticationToken(userDetail, null, userDetail.authorities)
        usernamePwAuthToken.details = WebAuthenticationDetailsSource().buildDetails(request)
        SecurityContextHolder.getContext().authentication = usernamePwAuthToken
        filterChain.doFilter(request, response)
    }

    private fun isInvalidToken(token: String) = !token.startsWith("Bearer ")
}
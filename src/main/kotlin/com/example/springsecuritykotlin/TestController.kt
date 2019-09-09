package com.example.springsecuritykotlin

import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.User
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping

@Controller
class TestController {
    @GetMapping
    fun index() = "index"

    @GetMapping("/profile")
    fun profile(auth: Authentication, model: Model): String {
        model.addAttribute("user", (auth.principal as? User))
        println(auth)
        return "profile"
    }
}
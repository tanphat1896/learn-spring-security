package com.example.researchanything.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping

@Controller
class TestController {
    @GetMapping
    fun index() = "index"

    @GetMapping("/profile")
    fun profile() = "profile"
}
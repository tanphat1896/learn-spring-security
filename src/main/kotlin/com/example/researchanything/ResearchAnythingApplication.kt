package com.example.researchanything

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

@SpringBootApplication
class ResearchAnythingApplication

fun main(args: Array<String>) {
	println(BCryptPasswordEncoder().encode("111111"))
	runApplication<ResearchAnythingApplication>(*args)
}

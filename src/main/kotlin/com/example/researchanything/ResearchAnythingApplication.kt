package com.example.researchanything

import com.example.researchanything.entity.User
import com.example.researchanything.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

@SpringBootApplication
class ResearchAnythingApplication : CommandLineRunner {
	@Autowired private lateinit var userRepository: UserRepository
	override fun run(vararg args: String?) {
		println(userRepository.findAll())
	}
}

fun main(args: Array<String>) {
	runApplication<ResearchAnythingApplication>(*args)
}

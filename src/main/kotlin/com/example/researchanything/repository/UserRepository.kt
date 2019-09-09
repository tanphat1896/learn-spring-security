package com.example.researchanything.repository

import com.example.researchanything.entity.User
import org.springframework.data.jpa.repository.JpaRepository
import java.util.Optional

interface UserRepository : JpaRepository<User, Int> {
    fun findByUsername(username: String): Optional<User>
}
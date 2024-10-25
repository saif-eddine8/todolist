package com.todolist_alx.todolist.service

import com.todolist_alx.todolist.controller.UserRequestDTO
import com.todolist_alx.todolist.model.CUser
import com.todolist_alx.todolist.repository.CUserRepository
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import kotlin.jvm.optionals.getOrElse

@Service
class CUserService (private val cUserRepository: CUserRepository): UserDetailsService {

    override fun loadUserByUsername(username: String): UserDetails =
        cUserRepository.findById(username)
        .getOrElse { throw UsernameNotFoundException(username) }

    @Throws(IllegalArgumentException::class)
    fun registerBy(user: UserRequestDTO) {
    usernameValidatorBy(user.username)
    passwordValidatorBy(user.password)
    // hashing password
    val hashedPassword = BCryptPasswordEncoder().encode(user.password)
    val newUser = CUser(
        name = user.username.lowercase(),
        pass = hashedPassword,
    )
        // save into db
        cUserRepository.save(newUser)
    }

    fun existByUsername(username: String): Boolean =
        cUserRepository.existsById(username)

    @Throws(IllegalArgumentException::class)
    fun usernameValidatorBy(username: String): Boolean {
        //Check username validity (min chars >= 3, allowed chars: -_, starting with a char)
        val usernamePattern = "^[a-zA-Z][a-zA-Z0-9-_]{2,}$".toRegex()
        if(!usernamePattern.matches(username)) {
            throw IllegalArgumentException("Username doesn't match the required pattern.")
        }

        // Check username availability
        if (existByUsername(username)) {
            throw IllegalArgumentException("Username exist.")
        }

        return true
    }

    @Throws(IllegalArgumentException::class)
    fun passwordValidatorBy(password: String): Boolean {
        // Check password validity (min length >= 8)
        val usernamePattern = "^.{8,}$".toRegex()
        if(!usernamePattern.matches(password)) {
            throw IllegalArgumentException("Password doesn't match the required pattern.")
        }

        return true
    }
}

package com.todolist_alx.todolist.controller

import com.todolist_alx.todolist.config.API_PREFIX
import com.todolist_alx.todolist.service.CUserService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(API_PREFIX)
class UserController (val cUserService: CUserService) {


    @PostMapping("/register")
    fun signUp(@RequestBody user: UserRequestDTO): ResponseEntity<String> {
        try {
            cUserService.registerBy(user)
        } catch (ex: IllegalArgumentException) {
            return ResponseEntity.badRequest().body(ex.message)
        } catch (ex: Exception) {
            ex.printStackTrace()
            return ResponseEntity.internalServerError().build()
        }

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build()
    }

}

data class UserRequestDTO(val username: String, val password: String);

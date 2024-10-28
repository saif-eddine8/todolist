package com.todolist_alx.todolist.controller

import com.todolist_alx.todolist.config.API_PREFIX
import com.todolist_alx.todolist.model.CUser
import com.todolist_alx.todolist.service.TodoListService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDateTime

@RestController
@RequestMapping("$API_PREFIX/todolist")
class TodoListController (val todoListService: TodoListService) {

    @GetMapping("/all")
    fun userTodoLists(authentication: Authentication): ResponseEntity<List<TodoListResponse>> {
        // Fetch all user todoLists.
        try {
            val userTodoLists = todoListService.findUserTodoListsBy(authentication.name)
            return ResponseEntity.ok(userTodoLists)
        } catch (ex: Exception) {
            ex.printStackTrace()
            return ResponseEntity.internalServerError().build()
        }
    }

    @GetMapping("/owned/all")
    fun userOwnedTodoLists(authentication: Authentication): ResponseEntity<List<TodoListResponse>> {
        // Fetch all user owned todoLists.
        try {
            val authenticatedUser = authentication.principal as CUser
            val userTodoLists = todoListService.findUserOwnedTodoListsBy(authenticatedUser)
            return ResponseEntity.ok(userTodoLists)
        } catch (ex: Exception) {
            ex.printStackTrace()
            return ResponseEntity.internalServerError().build()
        }
    }

    @GetMapping("/unowned/all")
    fun userUnownedTodoLists(authentication: Authentication): ResponseEntity<List<TodoListResponse>> {
        // Fetch all user unowned todoLists.
        try {
            val userTodoLists = todoListService.findUserUnownedTodoListsBy(authentication.name)
            return ResponseEntity.ok(userTodoLists)
        } catch (ex: Exception) {
            ex.printStackTrace()
            return ResponseEntity.internalServerError().build()
        }
    }

    @PostMapping("/add")
    fun addTodoList(@RequestBody todolistDTO: TodoListRequest, authentication: Authentication): ResponseEntity<String> {
        // Create a todolist instance and insert it into db.
        try {
            val authenticatedUser = authentication.principal as CUser
            todoListService.addNewTodoList(todolistDTO, authenticatedUser)
        } catch (ex: IllegalArgumentException) {
            return ResponseEntity.badRequest().body(ex.message)
        } catch (ex: Exception) {
            ex.printStackTrace()
            return ResponseEntity.internalServerError().build()
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build()
    }

    @DeleteMapping("/{todolistId}/delete")
    fun removeTodoList(@PathVariable(value = "todolistId") todolistId: Long, authentication: Authentication): ResponseEntity<String> {
        try {
            val authenticatedUser = authentication.principal as CUser
            todoListService.removeTodoListBy(todolistId, authenticatedUser)
        } catch (ex: IllegalArgumentException) {
            return ResponseEntity.badRequest().body(ex.message)
        } catch (ex: Exception) {
            ex.printStackTrace()
            return ResponseEntity.internalServerError().build()
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build()
    }

    //Add new member if it doesn't exist.
    @PostMapping("/{todolistId}/members/{username}/add")
    fun addNewMember(@PathVariable(value = "todolistId") todolistId: Long,
                     @PathVariable(value = "username") username: String,
                     authentication: Authentication
    ): ResponseEntity<String> {
        try {
            val authenticatedUser = authentication.principal as CUser
            todoListService.addNewMember(authenticatedUser, todolistId, username)
        } catch (ex: IllegalArgumentException) {
            return ResponseEntity.badRequest().body(ex.message)
        } catch (ex: Exception) {
            ex.printStackTrace()
            return ResponseEntity.internalServerError().build()
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build()
    }

    //Remove member if it doesn't exist.
    @DeleteMapping("/{todolistId}/members/{username}/remove")
    fun removeMember(@PathVariable(value = "todolistId") todolistId: Long,
                     @PathVariable(value = "username") username: String,
                     authentication: Authentication
    ): ResponseEntity<String> {
        try {
            val authenticatedUser = authentication.principal as CUser
            todoListService.removeMember(authenticatedUser, todolistId, username)
        } catch (ex: IllegalArgumentException) {
            return ResponseEntity.badRequest().body(ex.message)
        } catch (ex: Exception) {
            ex.printStackTrace()
            return ResponseEntity.internalServerError().build()
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build()
    }
}

data class TodoListRequest(val name: String, val description: String?);
data class TodoListResponse(val id: Long, val name: String,
                                val description: String?,
                                val createdAt: LocalDateTime,
                                val owner: String,
                                val members: List<String> = listOf(),
                                val elements: List<TodoListElementResponse> = listOf());

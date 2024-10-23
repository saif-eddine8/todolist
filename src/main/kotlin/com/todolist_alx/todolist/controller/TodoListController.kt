package com.todolist_alx.todolist.controller

import com.todolist_alx.todolist.config.API_PREFIX
import com.todolist_alx.todolist.model.CUser
import com.todolist_alx.todolist.model.TodoList
import com.todolist_alx.todolist.service.TodoListService
import jakarta.websocket.server.PathParam
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("$API_PREFIX/todolist")
class TodoListController (val todoListService: TodoListService) {

//    @GetMapping("/all")
//    fun userOwnedTodoLists(authentication: Authentication): ResponseEntity<List<TodoList>> {
//        // TODO: fetch all user owned todoLists.
//    }

    @PostMapping("/add")
    fun addTodoList(@RequestBody todolistDTO: TodoListRequestDTO, authentication: Authentication): ResponseEntity<String> {
        // Create a todolist instance and insert it into db.
        val authenticatedUser = authentication.principal as CUser
        try {
            todoListService.addNewTodoList(todolistDTO, authenticatedUser)
        } catch (ex: IllegalArgumentException) {
            return ResponseEntity.badRequest().body(ex.message)
        } catch (ex: Exception) {
            ex.printStackTrace()
            return ResponseEntity.internalServerError().build()
        }

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build()
    }

    @DeleteMapping("/delete")
    fun removeTodoList(@PathParam(value = "todolistId") todolistId: Long, authentication: Authentication): ResponseEntity<String> {
        val authenticatedUser = authentication.principal as CUser
        try {
            todoListService.removeTodoListBy(todolistId, authenticatedUser)
        } catch (ex: IllegalArgumentException) {
            return ResponseEntity.badRequest().body(ex.message)
        } catch (ex: Exception) {
            ex.printStackTrace()
            return ResponseEntity.internalServerError().build()
        }

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build()
    }

    @PostMapping("/members/add")
    fun addNewMember(): ResponseEntity<String> {
        //TODO: Add new member if doesn't exist.

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build()
    }

    @DeleteMapping("/members/remove")
    fun removeMember(): ResponseEntity<String> {
        //TODO: Remove member if doesn't exist.

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build()
    }
}

data class TodoListRequestDTO(val name: String, val description: String);

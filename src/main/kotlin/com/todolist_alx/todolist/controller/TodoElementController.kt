package com.todolist_alx.todolist.controller

import com.todolist_alx.todolist.config.API_PREFIX
import com.todolist_alx.todolist.model.CUser
import com.todolist_alx.todolist.service.ElementService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDateTime

@RestController
@RequestMapping("$API_PREFIX/todolist/{id}/element")
class TodoElementController(val todoElementService: ElementService) {

    @PostMapping("/{elementName}/add")
    fun addElement (@PathVariable(value = "id") todolistId: Long,
                    @PathVariable(value = "elementName") elementName: String,
                    authentication: Authentication
    ): ResponseEntity<String> {
        try {
            val authenticatedUser = authentication.principal as CUser
            todoElementService.addElement(authenticatedUser = authenticatedUser,
                    todoElementName = elementName, todoListId = todolistId )

        } catch (ex: IllegalArgumentException) {
            return ResponseEntity.badRequest().body(ex.message)
        } catch (ex: Exception) {
            ex.printStackTrace()
            return ResponseEntity.internalServerError().build()
        }

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build()
    }

    @DeleteMapping("/{elementName}/delete")
    fun removeElement (@PathVariable(value = "id") todolistId: Long,
                       @PathVariable(value = "elementName") elementName: String,
                       authentication: Authentication): ResponseEntity<String> {
        try {
            val authenticatedUser = authentication.principal as CUser
            todoElementService.removeElement(authenticatedUser = authenticatedUser,
                todoElementName = elementName, todoListId = todolistId )

        } catch (ex: IllegalArgumentException) {
            return ResponseEntity.badRequest().body(ex.message)
        } catch (ex: Exception) {
            ex.printStackTrace()
            return ResponseEntity.internalServerError().build()
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build()
    }

    @PutMapping("/{elementName}/complete")
    fun markElementAsDone (@PathVariable(value = "id") todolistId: Long,
                           @PathVariable(value = "elementName") elementName: String,
                           authentication: Authentication,
                           @RequestParam(required = false) markAsCompleted: Boolean = false): ResponseEntity<String> {
        try {
            val authenticatedUser = authentication.principal as CUser
            todoElementService.markElementAsDone(authenticatedUser = authenticatedUser,
                todoElementName = elementName, todoListId = todolistId, markAsCompleted = markAsCompleted )

        } catch (ex: IllegalArgumentException) {
            return ResponseEntity.badRequest().body(ex.message)
        } catch (ex: Exception) {
            ex.printStackTrace()
            return ResponseEntity.internalServerError().build()
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build()
    }

}

data class TodoListElementResponse(val name: String,
                                val createdAt: LocalDateTime,
                                val createdBy: String,
                                val completed: Boolean = false,
                                val completedBy: String? = null);
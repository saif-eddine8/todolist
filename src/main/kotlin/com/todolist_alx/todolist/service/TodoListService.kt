package com.todolist_alx.todolist.service

import com.todolist_alx.todolist.controller.TodoListRequestDTO
import com.todolist_alx.todolist.model.CUser
import com.todolist_alx.todolist.model.TodoList
import com.todolist_alx.todolist.repository.TodoListRepository
import org.springframework.stereotype.Service

@Service
class TodoListService(val todoListRepository: TodoListRepository) {

    @Throws(IllegalArgumentException::class)
    fun addNewTodoList(todoListDTO: TodoListRequestDTO, authenticatedUser: CUser) {
        isValidTodoListName(todoListDTO.name)
        val newTodoList = TodoList(
            id = null,
            name = todoListDTO.name,
            description = todoListDTO.description,
            owner = authenticatedUser,
            elements = null,
            members = null,
        )
        todoListRepository.save(newTodoList)
    }

    fun isValidTodoListName(todoListName: String): Boolean {
        //Check todolist validity (min chars > 4, allowed chars: -_, starting with a char)
        val pattern = "^[a-zA-Z][a-zA-Z0-9-_]{4,}$".toRegex()
        if(!pattern.matches(todoListName)) {
            throw IllegalArgumentException("Todolist name doesn't match the required pattern.")
        }
        return true
    }

    @Throws(IllegalArgumentException::class)
    fun removeTodoListBy(todoListId: Long, authenticatedUser: CUser) {
        isTodolistOwner(todoListId, authenticatedUser)
            .let { todoListRepository.delete(it) }
    }

    @Throws(IllegalArgumentException::class)
    fun isTodolistOwner(todoListId: Long, authenticatedUser: CUser): TodoList {
        val todolist = todoListRepository.findById(todoListId).orElseThrow{
            throw IllegalArgumentException("Todolist doesn't exist.")
        }
        if(todolist.owner.name != authenticatedUser.name) {
            throw IllegalArgumentException("To remove this todolist you must be the owner.")
        }

        return todolist
    }
}
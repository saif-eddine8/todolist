package com.todolist_alx.todolist.service

import com.todolist_alx.todolist.model.CUser
import com.todolist_alx.todolist.model.TodoElement
import com.todolist_alx.todolist.repository.TodoElementRepository
import com.todolist_alx.todolist.repository.TodoListRepository
import org.springframework.stereotype.Service

@Service
class ElementService (val elementRepository: TodoElementRepository,
                      val todoListRepository: TodoListRepository) {

    @Throws(IllegalArgumentException::class)
    fun addElement (authenticatedUser: CUser, todoElementName: String, todoListId: Long) {

        todoElementNameValidatorBy(todoElementName)

        val todolist = todoListRepository.findById(todoListId).orElseThrow {
            throw IllegalArgumentException("The giving todolist id doesn't exist.")
        }

        todolist.elements.find { element -> element.name == todoElementName }?.let {
            throw IllegalArgumentException("The giving todo element already exist.")
        }

        todolist.members.find { user -> user.name == authenticatedUser.name }.also {
            if (it == null && todolist.owner.name != authenticatedUser.name) {
                throw IllegalArgumentException("Authenticated user must be todolist owner or a member.")
            }
        }

        val todoElement = TodoElement(name = todoElementName, createdBy = authenticatedUser, todolist = todolist)

        // Add todoElement into DB.
        elementRepository.save(todoElement)
    }

    @Throws(IllegalArgumentException::class)
    fun removeElement (authenticatedUser: CUser, todoElementName: String, todoListId: Long) {
        todoElementNameValidatorBy(todoElementName)

        val todolist = todoListRepository.findById(todoListId).orElseThrow {
            throw IllegalArgumentException("The giving todolist id doesn't exist.")
        }

        val todoElement = todolist.elements.find { element -> element.name == todoElementName }
        if (todoElement == null) {
            throw IllegalArgumentException("The giving todo element doesn't exist.")
        }


        todolist.members.find { user -> user.name == authenticatedUser.name }.also {
            if (it == null && todolist.owner.name != authenticatedUser.name) {
                throw IllegalArgumentException("Authenticated user must be todolist owner or a member.")
            }
        }

        // Remove todoElement from DB.
        elementRepository.delete(todoElement)
    }

    @Throws(IllegalArgumentException::class)
    fun markElementAsDone (authenticatedUser: CUser, todoElementName: String, todoListId: Long, markAsCompleted: Boolean) {

        todoElementNameValidatorBy(todoElementName)

        val todolist = todoListRepository.findById(todoListId).orElseThrow {
            throw IllegalArgumentException("The giving todolist id doesn't exist.")
        }

        val todoElement = todolist.elements.find { element -> element.name == todoElementName }
        if (todoElement == null) {
            throw IllegalArgumentException("The giving todo element doesn't exist.")
        }

        if (todoElement.done == markAsCompleted) {
            throw IllegalArgumentException("The giving todo element is already updated, no change required.")
        }

        todolist.members.find { user -> user.name == authenticatedUser.name }.also {
            if (it == null && todolist.owner.name != authenticatedUser.name) {
                throw IllegalArgumentException("Authenticated user must be todolist owner or a member.")
            }
        }

        todoElement.done = markAsCompleted
        todoElement.markedAsDoneBy = if(markAsCompleted) authenticatedUser else null

        // Remove todoElement from DB.
        elementRepository.save(todoElement)
    }

    @Throws(IllegalArgumentException::class)
    fun todoElementNameValidatorBy(todoElementName: String): Boolean {
        // Check todoElement name validity (min chars >= 3, allowed chars: -_, starting with a char)
        val todoElementNamePattern = "^[a-zA-Z][a-zA-Z0-9-_]{2,}$".toRegex()
        if(!todoElementNamePattern.matches(todoElementName)) {
            throw IllegalArgumentException("Todo element name doesn't match the required pattern.")
        }

        return true
    }
}
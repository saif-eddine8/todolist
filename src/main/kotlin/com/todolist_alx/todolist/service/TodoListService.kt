package com.todolist_alx.todolist.service

import com.todolist_alx.todolist.controller.TodoListElementResponse
import com.todolist_alx.todolist.controller.TodoListRequest
import com.todolist_alx.todolist.controller.TodoListResponse
import com.todolist_alx.todolist.model.CUser
import com.todolist_alx.todolist.model.TodoList
import com.todolist_alx.todolist.repository.CUserRepository
import com.todolist_alx.todolist.repository.TodoListRepository
import org.springframework.stereotype.Service

@Service
class TodoListService(val todoListRepository: TodoListRepository, val cUserRepository: CUserRepository) {

    @Throws(IllegalArgumentException::class)
    fun addNewTodoList(todoListDTO: TodoListRequest, authenticatedUser: CUser) {
        todoListNameValidatorBy(todoListDTO.name)
        val newTodoList = TodoList(
            name = todoListDTO.name,
            description = todoListDTO.description,
            owner = authenticatedUser,
        )
        todoListRepository.save(newTodoList)
    }

    fun todoListNameValidatorBy(todoListName: String): Boolean {
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

    fun findUserTodoListsBy(username: String): List<TodoListResponse> =
        cUserRepository.findById(username).get().let { sequenceOf(it.ownedTodoLists, it.unownedTodoLists) }
            .flatten().toList()
            .map { todolist ->
                todoListResponseMapping(todolist)
        }

    fun findUserOwnedTodoListsBy(owner: CUser): List<TodoListResponse> =
        todoListRepository.findAllByOwner(owner).map { todolist ->
            todoListResponseMapping(todolist)
        }

    fun findUserUnownedTodoListsBy(username: String): List<TodoListResponse> =
        cUserRepository.findById(username).get().unownedTodoLists.map { todolist ->
            todoListResponseMapping(todolist)
        }


    @Throws(IllegalArgumentException::class)
    fun addNewMember(authenticatedUser: CUser, todoListId: Long, memberUsername: String) {

        val todolist = todoListRepository.findById(todoListId).orElseThrow {
            throw IllegalArgumentException("The giving todolist id doesn't exist.")
        }

         if (todolist.owner.name != authenticatedUser.name) {
             throw IllegalArgumentException("Operation not allowed, authenticated user must be the todolist owner.")
         }

        val member = cUserRepository.findById(memberUsername).orElseThrow{
            throw IllegalArgumentException("The giving user doesn't exist.")
        }

        if (todolist.owner.name == member.name) {
            throw IllegalArgumentException("Operation not allowed, owner can't be added as a todolist member.")
        }

        todolist.members.find { user -> user.name == member.name }?.let {
            throw IllegalArgumentException("The giving user is already a member.")
        }

        // Add new member to the todolist members.
        todolist.members.add(member)

        // Update todolist members in DB.
        todoListRepository.save(todolist)
    }

    @Throws(IllegalArgumentException::class)
    fun removeMember(authenticatedUser: CUser, todoListId: Long, memberUsername: String) {

        val todolist = todoListRepository.findById(todoListId).orElseThrow {
            throw IllegalArgumentException("The giving todolist id doesn't exist.")
        }

        if (todolist.owner.name != authenticatedUser.name) {
            throw IllegalArgumentException("Operation not allowed, authenticated user must be the todolist owner.")
        }

        val member = cUserRepository.findById(memberUsername).orElseThrow{
            throw IllegalArgumentException("The giving user doesn't exist.")
        }

        todolist.members.find { user -> user.name == member.name }.also {
            if (it == null) {
                throw IllegalArgumentException("The giving user isn't a member of the todolist.")
            }
        }

        // Remove member to the todolist members.
        todolist.members.remove(member)

        // Update todolist members in DB.
        todoListRepository.save(todolist)
    }

    fun todoListResponseMapping(todolist: TodoList): TodoListResponse =
        TodoListResponse(
            id = todolist.id,
            name = todolist.name,
            description = todolist.description,
            createdAt = todolist.createdAt,
            owner = todolist.owner.name,
            members = todolist.members.map { member -> member.username },
            elements = todolist.elements.map { element -> TodoListElementResponse(
                name = element.name,
                createdAt = element.createdAt,
                createdBy = element.createdBy.name,
                completed = element.done,
                completedBy = element.markedAsDoneBy?.name
                ) }
          )

}
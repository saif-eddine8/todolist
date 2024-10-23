package com.todolist_alx.todolist.repository

import com.todolist_alx.todolist.model.TodoList
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface TodoListRepository: JpaRepository<TodoList, Long>
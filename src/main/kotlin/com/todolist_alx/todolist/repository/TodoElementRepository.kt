package com.todolist_alx.todolist.repository

import com.todolist_alx.todolist.model.TodoElement
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface TodoElementRepository: JpaRepository<TodoElement, String>
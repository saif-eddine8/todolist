package com.todolist_alx.todolist.model

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.ManyToOne
import java.io.Serializable
import java.time.LocalDateTime

@Entity
data class TodoElement(@Id val name: String,
                       @ManyToOne val createdBy: CUser,
                       @ManyToOne val todolist: TodoList,
                       val  createdAt: LocalDateTime = LocalDateTime.now(),
                       var done: Boolean = false,
                       @ManyToOne
                       var markedAsDoneBy: CUser? = null
                       ): Serializable

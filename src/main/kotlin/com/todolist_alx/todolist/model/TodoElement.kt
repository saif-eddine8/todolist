package com.todolist_alx.todolist.model

import jakarta.persistence.*
import java.io.Serializable
import java.time.LocalDateTime

@Entity
data class TodoElement(@Id @GeneratedValue(strategy = GenerationType.AUTO) val id: Long,
                       val name: String,
                       @ManyToOne val createdBy: CUser,
                       @ManyToOne val todolist: TodoList,
                       val  createdAt: LocalDateTime? = LocalDateTime.now(),
                       val done: Boolean? = false,
                       ): Serializable

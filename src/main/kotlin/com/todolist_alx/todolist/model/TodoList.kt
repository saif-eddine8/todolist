package com.todolist_alx.todolist.model

import jakarta.persistence.*
import java.io.Serializable
import java.time.LocalDateTime

@Entity
data class TodoList(@Id @GeneratedValue(strategy = GenerationType.AUTO) val id: Long?,
                    val name: String,
                    val description: String,
                    @ManyToOne val owner: CUser,
                    @ManyToMany val members: List<CUser>?,
                    @OneToMany(mappedBy = "todolist") val elements: List<TodoElement>?,
                    val createdAt: LocalDateTime? = LocalDateTime.now(),
                    ): Serializable

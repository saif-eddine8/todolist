package com.todolist_alx.todolist.model

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.ManyToMany
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToMany
import java.io.Serializable
import java.time.LocalDateTime

@Entity
data class TodoList(@Id @GeneratedValue(strategy = GenerationType.IDENTITY) val id: Long = -1,
                    val name: String,
                    val description: String?,
                    @ManyToOne val owner: CUser,
                    @ManyToMany val members: MutableList<CUser> = mutableListOf(),
                    @OneToMany(mappedBy = "todolist") val elements: MutableList<TodoElement> = mutableListOf(),
                    val createdAt: LocalDateTime = LocalDateTime.now(),
                    ): Serializable

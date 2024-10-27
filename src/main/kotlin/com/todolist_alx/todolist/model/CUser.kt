package com.todolist_alx.todolist.model

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.ManyToMany
import jakarta.persistence.OneToMany
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.io.Serializable

@Entity
class CUser(
    @Id val name: String,
    val pass: String,
    @OneToMany(mappedBy = "owner") val ownedTodoLists: MutableList<TodoList> = mutableListOf(),
    @ManyToMany(mappedBy = "members") val unownedTodoLists: MutableList<TodoList> = mutableListOf(),
    @OneToMany(mappedBy = "createdBy") val todoElements: MutableList<TodoElement> = mutableListOf(),
    @OneToMany(mappedBy = "markedAsDoneBy") val completedTodoElements: MutableList<TodoElement> = mutableListOf()
) : UserDetails, Serializable {
    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        return mutableListOf()
    }

    override fun getPassword(): String {
        return pass
    }

    override fun getUsername(): String {
        return name
    }
}
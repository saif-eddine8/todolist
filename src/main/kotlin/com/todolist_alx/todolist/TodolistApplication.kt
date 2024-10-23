package com.todolist_alx.todolist

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class TodolistApplication

fun main(args: Array<String>) {
	runApplication<TodolistApplication>(*args)
}

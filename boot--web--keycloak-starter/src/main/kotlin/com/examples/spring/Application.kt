package com.examples.spring

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@SpringBootApplication
class Application

@RestController
@RequestMapping("/api/todos")
class TodoController {
    val todos = mutableListOf<String>()

    @GetMapping
    fun readTodos(): List<String> =
        todos

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createTodo(@RequestBody todo: String) {
        todos += todo
    }
}

fun main(args: Array<String>) {
    runApplication<Application>(*args)
}

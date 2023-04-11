package com.api.heys

import org.springframework.boot.runApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.annotations.servers.Server

@OpenAPIDefinition(servers = [Server(url = "/")])
@SpringBootApplication
class HeysApplication

fun main(args: Array<String>) {
	runApplication<HeysApplication>(*args)
}

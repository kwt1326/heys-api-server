package com.api.heys.domain.common.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("common")
class CommonController {
    // AWS load balancer healthy check API. must not remove!
    @GetMapping("/ping")
    fun ping(): String {
        return "pong!"
    }
}
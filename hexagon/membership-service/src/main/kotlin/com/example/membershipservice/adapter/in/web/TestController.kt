package com.example.membershipservice.adapter.`in`.web

import lombok.RequiredArgsConstructor
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequiredArgsConstructor
class testController {
    @GetMapping("/test")
    fun test () {
        println("Hello World!")
    }
}
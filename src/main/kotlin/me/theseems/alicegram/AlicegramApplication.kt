package me.theseems.alicegram

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class AlicegramApplication

fun main(args: Array<String>) {
    runApplication<AlicegramApplication>(*args)
}

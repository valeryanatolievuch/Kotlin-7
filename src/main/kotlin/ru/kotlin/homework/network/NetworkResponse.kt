@file:Suppress("unused")

package ru.kotlin.homework.network

import java.time.LocalDateTime

/**
 * Network result
 */
sealed class NetworkResponse<out T, out R> {
    val responseDateTime: LocalDateTime = LocalDateTime.now()
}

/**
 * Network success
 */
data class Success<out T>(val resp: T): NetworkResponse<T, Nothing>()

/**
 * Network error
 */
data class Failure<out R>(val error: R): NetworkResponse<Nothing, R>()

val s1 = Success("Message")
val r11: NetworkResponse<String, Error> = s1
val r12: NetworkResponse<Any, Error> = s1

val e = Failure(Error())
val er1: NetworkResponse<String, Error> = e
val er2: NetworkResponse<Any, Error> = e
val er4: NetworkResponse<Any, Throwable> = e

val er5: NetworkResponse<Int, Throwable> = Failure(IllegalArgumentException("message"))

val message = e.error.message
@file:Suppress("unused")

package ru.kotlin.homework.network

import java.time.LocalDateTime

/**
 * Известный вам список ошибок
 */
sealed class ApiException(message: String) : Throwable(message) {
    data object NotAuthorized : ApiException("Not authorized") {
        private fun readResolve(): Any = NotAuthorized
    }

    data object NetworkException : ApiException("Not connected") {
        private fun readResolve(): Any = NetworkException
    }

    data object UnknownException: ApiException("Unknown exception") {
        private fun readResolve(): Any = UnknownException
    }
}

class ErrorLogger<in E : Throwable> {

    private val errors = mutableListOf<Pair<LocalDateTime, Throwable>>()

    fun log(response: NetworkResponse<*, @UnsafeVariance E>) {
        if (response is Failure) {
            errors.add(response.responseDateTime to response.error)
        }
    }
    fun dump(): List<Pair<LocalDateTime, Throwable>> {
        return errors.toList()
    }


    fun dumpLog() {
        errors.forEach { (date, error) ->
            println("Error at $date: ${error.message}")
        }
    }
}

fun processThrowables(logger: ErrorLogger<Throwable>) {
    logger.log(Failure(IllegalArgumentException("Something unexpected")))
    logger.dumpLog()
}

fun processApiErrors(apiExceptionLogger: ErrorLogger<ApiException>) {
    apiExceptionLogger.log(Failure(ApiException.NetworkException))
    apiExceptionLogger.dumpLog()
}

fun main() {
    val logger = ErrorLogger<Throwable>()

    println("Processing Throwable:")
    processThrowables(logger)

    println("Processing Api:")
    processApiErrors(logger)
}


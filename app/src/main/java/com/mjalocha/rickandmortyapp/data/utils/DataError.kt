package com.mjalocha.rickandmortyapp.data.utils

sealed interface DataError : Error {
    enum class Remote : DataError {
        REQUEST_TIMEOUT,
        TOO_MANY_REQUESTS,
        NO_INTERNET,
        NO_RESULTS,
        SERVER,
        SERIALIZATION,
        UNKNOWN
    }
}
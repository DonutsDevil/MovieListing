package com.swapnil.movielisting.data.mapper

abstract class ResponseMapper<T> {
    abstract fun map(value: String): T
}
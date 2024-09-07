package com.swapnil.movielisting.data

interface ApiClient<Response> {
    suspend fun get(endpoint: String): Result<Response>
    suspend fun get(endpoint: String, queryParameters: Map<String, String>): Result<Response>
}
package com.swapnil.movielisting.data

import com.swapnil.movielisting.data.mapper.ResponseMapper
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.parameter
import io.ktor.client.request.request
import io.ktor.client.request.url
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpMethod
import io.ktor.http.Url
import io.ktor.http.isSuccess

class KtorApiClient<Response>(
    private val baseUrl: String,
    private val responseMapper: ResponseMapper<Response>,
    private val httpClient: HttpClient
): ApiClient<Response> {

    override suspend fun get(endpoint: String): Result<Response> {
        val url = getUrl(endpoint)
        val response = try {
            request(url, emptyMap(), HttpMethod.Get)
        } catch (e: Exception) {
            Result.failure(e)
        }
        return handleResponse(response)
    }

    override suspend fun get(
        endpoint: String,
        queryParameters: Map<String, String>
    ): Result<Response> {
        val url = getUrl(endpoint)
        val response = try {
            request(url, queryParameters, HttpMethod.Get)
        } catch (e: Exception) {
            Result.failure(e)
        }
        return handleResponse(response)
    }

    private fun getUrl(endpoint: String) = Url("${baseUrl}${endpoint}")

    private suspend fun request(url: Url, queryParameters: Map<String, String>, method: HttpMethod): Result<HttpResponse> {
        val response = httpClient.request {
            url("$url")
            for ((key, value) in queryParameters) {
                parameter(key, value)
            }
            this.method = method
        }

        return if (response.status.isSuccess()) {
            Result.success(response)
        } else {
            Result.failure(HttpException(response.status.value, response.status.description, response))
        }
    }

    private suspend fun handleResponse(result: Result<HttpResponse>): Result<Response> {
        return if (result.isSuccess) {
            try {
                val response = result.getOrThrow()
                Result.success(responseMapper.map(response.body()))
            } catch (e: Exception) {
                Result.failure(e)
            }
        } else {
            Result.failure(result.exceptionOrNull()!!)
        }
    }
}


class HttpException(val code: Int, override val message: String, val response: HttpResponse): Exception(message) {
    override fun toString(): String {
        return "HttpException(code=$code, message='$message', response=$response)"
    }
}
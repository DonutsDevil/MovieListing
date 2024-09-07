package com.swapnil.movielisting.domain.model.search

interface Searchable {
    fun getQueryParameters(): Map<String, String>
}
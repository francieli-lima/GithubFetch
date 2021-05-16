package br.com.francielilima.githubfetch.network

import br.com.francielilima.githubfetch.entities.Repository
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterface {

    @GET("search/repositories")
    suspend fun getRepositories(@Query("q") query: String, @Query("page") page: Int): Search
}

class Search (val items: List<Repository>)
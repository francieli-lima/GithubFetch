package br.com.francielilima.githubfetch.features.home

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.ViewModel
import androidx.paging.PagingData
import br.com.francielilima.githubfetch.entities.Repository
import br.com.francielilima.githubfetch.network.ApiRepository
import kotlinx.coroutines.flow.Flow

class HomeViewModel(
    private val apiRepository: ApiRepository
) : ViewModel(), LifecycleObserver {

    private var currentQuery: String? = null

    private var currentResult: Flow<PagingData<Repository>>? = null

    fun getRepositories(query: String): Flow<PagingData<Repository>> {
        val lastResult = currentResult

        if (query == currentQuery && lastResult != null) {
            return lastResult
        }

        val result: Flow<PagingData<Repository>> = apiRepository.loadRepositories(query)

        currentQuery = query
        currentResult = result

        return result
    }

    fun getTrendingRepositories(): Flow<PagingData<Repository>> {
        return apiRepository.loadRepositories()
    }
}
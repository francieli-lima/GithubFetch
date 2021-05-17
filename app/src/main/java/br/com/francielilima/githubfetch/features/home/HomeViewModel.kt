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

    private var currentQueryValue: String? = null

    private var currentSearchResult: Flow<PagingData<Repository>>? = null

    fun getRepositories(query: String): Flow<PagingData<Repository>> {
        val lastResult = currentSearchResult

        if (query == currentQueryValue && lastResult != null) {
            return lastResult
        }

        currentQueryValue = query

        val newResult: Flow<PagingData<Repository>> = apiRepository.loadRepositories(query)
        currentSearchResult = newResult

        return newResult
    }

    fun getTrendingRepositories(): Flow<PagingData<Repository>> {
        return apiRepository.loadRepositories()
    }
}
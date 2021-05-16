package br.com.francielilima.githubfetch.features.home

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import br.com.francielilima.githubfetch.entities.Repository
import br.com.francielilima.githubfetch.network.ApiRepository
import kotlinx.coroutines.flow.Flow

class HomeViewModel(
    private val apiRepository: ApiRepository
) : ViewModel(), LifecycleObserver {

    private var currentQueryValue: String? = null

    private var currentSearchResult: Flow<PagingData<Repository>>? = null

    fun getRepositories(queryString: String): Flow<PagingData<Repository>> {
        val lastResult = currentSearchResult
        if (queryString == currentQueryValue && lastResult != null) {
            return lastResult
        }
        currentQueryValue = queryString
        val newResult: Flow<PagingData<Repository>> = apiRepository.getRepositories(queryString)
            .cachedIn(viewModelScope)
        currentSearchResult = newResult
        return newResult
    }
}
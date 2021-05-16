package br.com.francielilima.githubfetch.network

import android.content.Context
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import br.com.francielilima.githubfetch.database.RepositoryDao
import br.com.francielilima.githubfetch.entities.Repository
import br.com.francielilima.githubfetch.features.home.RepositoryPagingSource
import kotlinx.coroutines.flow.Flow

interface ApiRepository {
    fun getRepositories(query: String): Flow<PagingData<Repository>>
}

class ApiRepositoryImplementation(
    private val apiInterface: ApiInterface,
    private val context: Context,
    private val dao: RepositoryDao
) : ApiRepository {

    override fun getRepositories(query: String): Flow<PagingData<Repository>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { RepositoryPagingSource(apiInterface, query) }
        ).flow
    }
}



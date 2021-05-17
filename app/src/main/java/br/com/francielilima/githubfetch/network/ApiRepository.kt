package br.com.francielilima.githubfetch.network

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import br.com.francielilima.githubfetch.database.Database
import br.com.francielilima.githubfetch.entities.Repository
import br.com.francielilima.githubfetch.features.home.RepositoryMediator
import kotlinx.coroutines.flow.Flow

interface ApiRepository {

    fun loadRepositories(
        query: String = ""
    ): Flow<PagingData<Repository>>
}

@OptIn(ExperimentalPagingApi::class)
class ApiRepositoryImplementation(
    private val apiInterface: ApiInterface,
    private val database: Database
) : ApiRepository {

    override fun loadRepositories(
        query: String
    ): Flow<PagingData<Repository>> {

        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false
            ),
            remoteMediator = RepositoryMediator(
                apiInterface,
                query,
                database
            ),
            pagingSourceFactory = {
                val broaderQuery = "%$query%"
                if (query.isEmpty()) database.repositoryDao()
                    .loadSearch() else database.repositoryDao().loadSearch(broaderQuery)
            }
        ).flow
    }
}




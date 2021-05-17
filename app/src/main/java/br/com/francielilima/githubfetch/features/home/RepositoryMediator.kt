package br.com.francielilima.githubfetch.features.home

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import br.com.francielilima.githubfetch.database.Database
import br.com.francielilima.githubfetch.entities.Node
import br.com.francielilima.githubfetch.entities.Repository
import br.com.francielilima.githubfetch.network.ApiInterface
import retrofit2.HttpException
import java.io.IOException

private const val POPULAR_REPOS_QUERY = "stars:\">1000\""

@OptIn(ExperimentalPagingApi::class)
class RepositoryMediator(
    private val apiInterface: ApiInterface,
    private val query: String,
    private val database: Database
) : RemoteMediator<Int, Repository>() {

    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, Repository>
    ): MediatorResult {
        val page = when (loadType) {
            LoadType.REFRESH -> {
                val page = getNode(state.anchorPosition?.let { position ->
                    state.closestItemToPosition(position)?.id
                })
                page?.nextPage?.minus(1) ?: 1
            }
            LoadType.PREPEND -> {
                val page =
                    getNode(state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()?.id)
                page?.previousPage
                    ?: return MediatorResult.Success(endOfPaginationReached = page != null)
            }
            LoadType.APPEND -> {
                val page =
                    getNode(state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()?.id)
                page?.nextPage
                    ?: return MediatorResult.Success(endOfPaginationReached = page != null)
            }
        }

        try {
            val apiResponse = apiInterface.getRepositories(
                if (query.isEmpty()) POPULAR_REPOS_QUERY else query,
                page
            )

            val repositories = apiResponse.items
            val allResultsLoaded = repositories.isEmpty()

            database.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    database.nodeDao().deleteAll()
                    database.repositoryDao().deleteAll()
                }

                val previousPage = if (page == 1) null else page - 1
                val nextPage = if (allResultsLoaded) null else page + 1

                val node = repositories.map {
                    Node(id = it.id, previousPage = previousPage, nextPage = nextPage)
                }

                database.nodeDao().insertAll(node)
                database.repositoryDao().insertAll(repositories)
            }
            return MediatorResult.Success(endOfPaginationReached = allResultsLoaded)
        } catch (exception: IOException) {
            return MediatorResult.Error(exception)
        } catch (exception: HttpException) {
            return MediatorResult.Error(exception)
        }
    }

    private suspend fun getNode(id: Long?): Node? {
        return id?.let { database.nodeDao().loadPageById(id) }
    }
}
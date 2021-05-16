package br.com.francielilima.githubfetch.features.home

import androidx.paging.PagingSource
import androidx.paging.PagingState
import br.com.francielilima.githubfetch.entities.Repository
import br.com.francielilima.githubfetch.network.ApiInterface
import retrofit2.HttpException
import java.io.IOException

class RepositoryPagingSource(
    private val apiInterface: ApiInterface,
    private val query: String,
    private val sort: String?,
    private val order: String?
) : PagingSource<Int, Repository>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Repository> {
        val position = params.key ?: 1
        return try {
            val repositories = apiInterface.getRepositories(query, position, sort, order).items
            val nextKey = if (repositories.isEmpty()) null else position + (params.loadSize / 30)

            LoadResult.Page(
                data = repositories,
                prevKey = if (position == 1) null else position - 1,
                nextKey = nextKey
            )
        } catch (exception: IOException) {
            return LoadResult.Error(exception)
        } catch (exception: HttpException) {
            return LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Repository>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}
package br.com.francielilima.githubfetch.network

import androidx.paging.*
import androidx.room.Room
import br.com.francielilima.githubfetch.database.Database
import br.com.francielilima.githubfetch.entities.Repository
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
@ExperimentalPagingApi
class ApiRepositoryTest {

    private val coroutineDispatcher = TestCoroutineDispatcher()
    private lateinit var database: Database
    private var apiInterface: ApiInterface = mockk()
    private val repositories = listOf(Repository(), Repository())

    private lateinit var apiRepository: ApiRepository

    private val testDispatcher = TestCoroutineDispatcher()
    private val testScope = TestCoroutineScope(testDispatcher)

    @Before
    fun setup() = coroutineDispatcher.runBlockingTest {
        database = Room.inMemoryDatabaseBuilder(
            mockk(), Database::class.java
        ).build()

        apiRepository = ApiRepositoryImplementation(apiInterface, database)
    }

    @Test
    fun verifyIfRepositoriesAreLoading() {
        coEvery { apiInterface.getRepositories(any(), any(), any()) } returns Search(repositories)

        testScope.launch {
            apiRepository.loadRepositories().collect { pagingData ->
                val result = pagingData.collectData()

                assertEquals(result, repositories)
            }
        }
    }

    private suspend fun <T : Any> PagingData<T>.collectData(): List<T> {
        val dcb = object : DifferCallback {
            override fun onChanged(position: Int, count: Int) {}
            override fun onInserted(position: Int, count: Int) {}
            override fun onRemoved(position: Int, count: Int) {}
        }
        val items = mutableListOf<T>()
        val dif = object : PagingDataDiffer<T>(dcb, TestCoroutineDispatcher()) {
            override suspend fun presentNewList(
                previousList: NullPaddedList<T>,
                newList: NullPaddedList<T>,
                newCombinedLoadStates: CombinedLoadStates,
                lastAccessedIndex: Int,
                onListPresentable: () -> Unit
            ): Int? {
                for (idx in 0 until newList.size)
                    items.add(newList.getFromStorage(idx))
                return null
            }
        }
        dif.collectFrom(this)
        return items
    }
}


package br.com.francielilima.githubfetch.database

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import br.com.francielilima.githubfetch.entities.Repository

@Dao
interface RepositoryDao {

    @Query("SELECT * FROM repository WHERE name LIKE :query OR description LIKE :query ORDER BY stars DESC, name ASC")
    fun loadSearch(query: String): PagingSource<Int, Repository>

    @Query("SELECT * FROM repository ORDER BY stars DESC, name ASC")
    fun loadSearch(): PagingSource<Int, Repository>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(repositories: List<Repository>)

    @Query("DELETE FROM repository")
    suspend fun deleteAll()
}
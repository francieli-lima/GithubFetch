package br.com.francielilima.githubfetch.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import br.com.francielilima.githubfetch.entities.Repository

@Dao
interface RepositoryDao {

    @Query("SELECT * from repository")
    fun load(): List<Repository>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(repositories: List<Repository>)
}
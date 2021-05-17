package br.com.francielilima.githubfetch.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import br.com.francielilima.githubfetch.entities.Node

@Dao
interface NodeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(remoteKey: List<Node>)

    @Query("SELECT * FROM page WHERE id = :id")
    suspend fun loadPageById(id: Long): Node?

    @Query("DELETE FROM page")
    suspend fun deleteAll()
}
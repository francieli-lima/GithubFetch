package br.com.francielilima.githubfetch.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import br.com.francielilima.githubfetch.entities.Owner
import br.com.francielilima.githubfetch.entities.Repository

@Database(entities = [Repository::class, Owner::class], version = 1)
@TypeConverters(OwnerConverter::class)
abstract class Database : RoomDatabase() {
    abstract fun repositoryDao(): RepositoryDao
}

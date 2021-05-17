package br.com.francielilima.githubfetch.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import br.com.francielilima.githubfetch.entities.Node
import br.com.francielilima.githubfetch.entities.Owner
import br.com.francielilima.githubfetch.entities.Repository

@Database(entities = [Repository::class, Owner::class, Node::class], version = 1)
@TypeConverters(OwnerConverter::class)
abstract class Database : RoomDatabase() {
    abstract fun repositoryDao(): RepositoryDao
    abstract fun nodeDao(): NodeDao
}

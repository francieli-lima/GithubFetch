package br.com.francielilima.githubfetch.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "page")
data class Node(
    @PrimaryKey
    val id: Long,
    val previousPage: Int?,
    val nextPage: Int?
)
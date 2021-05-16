package br.com.francielilima.githubfetch.entities

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "owner")
data class Owner(
    @PrimaryKey var id: String = "",
    var login: String = "",
    @SerializedName("avatar_url") var avatarUrl: String = ""
) : Parcelable
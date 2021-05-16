package br.com.francielilima.githubfetch.entities

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "repository")
@Parcelize
data class Repository(
    @PrimaryKey var id: String = "",
    var name: String = "",
    @SerializedName("full_name") var fullName: String = "",
    var owner: Owner? = null,
    var private: Boolean = false,
    var description: String? = "",
    @SerializedName("html_url") var url: String = "",
    @SerializedName("updated_at") var lastUpdate: String = ""
) : Parcelable